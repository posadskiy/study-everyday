package com.posadskiy.orchestration.payment.lab11;

import com.posadskiy.orchestration.payment.refund.RefundActivities;
import com.posadskiy.orchestration.payment.refund.RefundRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.WorkflowReplayer;
import io.temporal.worker.Worker;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference solution for the module 1.1 capstone (session 10): a refund workflow with a
 * {@code getStage()} query and a {@code cancel} signal that is honoured only before the PSP call.
 *
 * <p>Kept as a test-scope copy so the real {@code RefundWorkflow} stays the learner's exercise.
 */
class CapstoneReferenceTest {

    private static final String QUEUE = "capstone-queue";

    @WorkflowInterface
    public interface CancellableRefund {

        @WorkflowMethod
        String process(RefundRequest request);

        @SignalMethod
        void cancel(String reason);

        @QueryMethod
        String getStage();

        class Impl implements CancellableRefund {

            private final RefundActivities activities = Workflow.newActivityStub(
                    RefundActivities.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(30)).build());

            private String stage = "STARTED";
            private boolean cancelRequested;

            @Override
            public String process(RefundRequest request) {
                activities.validate(request);
                stage = "VALIDATED";
                String reservationId = activities.reserveFunds(request);
                stage = "RESERVED";

                // Cut-off: after the PSP call starts, money may have moved, so cancelling is no
                // longer a simple stop (that is the saga problem of module 1.3).
                Workflow.await(Duration.ofSeconds(5), () -> cancelRequested);
                if (cancelRequested) {
                    stage = "CANCELLED";
                    return "CANCELLED";
                }

                String pspReference = activities.submitToPsp(request, reservationId);
                stage = "PSP_SUBMITTED";
                activities.postLedger(request, pspReference);
                stage = "LEDGER_POSTED";
                activities.notifyCustomer(request, pspReference);
                stage = "COMPLETED";
                return "COMPLETED";
            }

            @Override
            public void cancel(String reason) {
                cancelRequested = true;
            }

            @Override
            public String getStage() {
                return stage;
            }
        }
    }

    static class FakeActivities implements RefundActivities {
        final List<String> calls = new CopyOnWriteArrayList<>();
        volatile Consumer<String> duringPsp = ref -> {};

        @Override
        public void validate(RefundRequest r) {
            calls.add("validate");
        }

        @Override
        public String reserveFunds(RefundRequest r) {
            calls.add("reserve");
            return "res-" + r.refundId();
        }

        @Override
        public String submitToPsp(RefundRequest r, String reservationId) {
            calls.add("psp");
            duringPsp.accept(r.refundId());
            return "psp-" + r.refundId();
        }

        @Override
        public void postLedger(RefundRequest r, String pspReference) {
            calls.add("ledger");
        }

        @Override
        public void notifyCustomer(RefundRequest r, String pspReference) {
            calls.add("notify");
        }
    }

    private static final RefundRequest REQUEST = new RefundRequest("r-1", "p-1", 1999, "EUR");

    private TestWorkflowEnvironment env;
    private WorkflowClient client;
    private FakeActivities activities;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(CancellableRefund.Impl.class);
        activities = new FakeActivities();
        worker.registerActivitiesImplementations(activities);
        env.start();
        client = env.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    private CancellableRefund start(String id) {
        CancellableRefund workflow = client.newWorkflowStub(
                CancellableRefund.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId(id).build());
        WorkflowClient.start(workflow::process, REQUEST);
        return workflow;
    }

    private List<EventType> types(String id) {
        return client.fetchHistory(id).getEvents().stream().map(e -> e.getEventType()).toList();
    }

    @Test
    void stageEndsAtCompletedAndCanBeQueriedAfterwards() {
        CancellableRefund workflow = start("wf-stage");

        assertThat(WorkflowStub.fromTyped(workflow).getResult(String.class)).isEqualTo("COMPLETED");

        assertThat(workflow.getStage()).isEqualTo("COMPLETED");
    }

    @Test
    void cancelBeforePspStopsTheRefund() {
        CancellableRefund workflow = start("wf-cancel-early");
        workflow.cancel("customer changed their mind");

        assertThat(WorkflowStub.fromTyped(workflow).getResult(String.class)).isEqualTo("CANCELLED");

        assertThat(activities.calls).containsExactly("validate", "reserve"); // PSP never called
        assertThat(workflow.getStage()).isEqualTo("CANCELLED");
    }

    @Test
    void cancelAfterPspStartedIsRecordedButIgnored() {
        CancellableRefund workflow = client.newWorkflowStub(
                CancellableRefund.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-cancel-late").build());
        activities.duringPsp = ref -> workflow.cancel("too late");
        WorkflowClient.start(workflow::process, REQUEST);

        assertThat(WorkflowStub.fromTyped(workflow).getResult(String.class)).isEqualTo("COMPLETED");

        assertThat(activities.calls).containsExactly("validate", "reserve", "psp", "ledger", "notify");
        assertThat(types("wf-cancel-late")).contains(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_SIGNALED);
    }

    @Test
    void queryLeavesNoTraceInHistory() {
        CancellableRefund workflow = start("wf-query");
        WorkflowStub.fromTyped(workflow).getResult(String.class);
        int before = types("wf-query").size();

        workflow.getStage();
        workflow.getStage();

        assertThat(types("wf-query")).hasSize(before);
    }

    @Test
    void recordedHistoryStillReplaysAgainstTheCode() throws Exception {
        CancellableRefund workflow = start("wf-replay");
        WorkflowStub.fromTyped(workflow).getResult(String.class);

        WorkflowReplayer.replayWorkflowExecution(
                client.fetchHistory("wf-replay"), CancellableRefund.Impl.class);
    }
}
