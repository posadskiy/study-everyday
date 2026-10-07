package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Lab 8 — what the engine does when an activity fails.
 *
 * <p>Same refund shape, but the PSP step is flaky. Shows retries, the attempt counter, a
 * non-retryable failure, retry exhaustion, and what none of them put in the history.
 */
class Lab08RetriesAndFailuresTest {

    private static final String QUEUE = "retry-queue";

    @io.temporal.activity.ActivityInterface
    public interface PspActivities {
        String refund(String refundId);
    }

    /** Fails with a transient error for the first {@code failFirst} calls. */
    static class FlakyPsp implements PspActivities {
        final AtomicInteger calls = new AtomicInteger();
        final List<Integer> attemptsSeen = new CopyOnWriteArrayList<>();
        volatile int failFirst;
        volatile boolean permanentFailure;

        @Override
        public String refund(String refundId) {
            int call = calls.incrementAndGet();
            attemptsSeen.add(Activity.getExecutionContext().getInfo().getAttempt());
            if (permanentFailure) {
                throw ApplicationFailure.newNonRetryableFailure("card closed", "CardClosed");
            }
            if (call <= failFirst) {
                throw new IllegalStateException("PSP returned 503");
            }
            return "psp-" + refundId;
        }
    }

    @WorkflowInterface
    public interface PspWorkflow {
        @WorkflowMethod
        String run(String refundId);

        class Impl implements PspWorkflow {
            private final PspActivities psp = Workflow.newActivityStub(
                    PspActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(4)
                                    .setInitialInterval(Duration.ofSeconds(1))
                                    .setBackoffCoefficient(2.0)
                                    .build())
                            .build());

            @Override
            public String run(String refundId) {
                return psp.refund(refundId);
            }
        }
    }

    private TestWorkflowEnvironment env;
    private WorkflowClient client;
    private FlakyPsp psp;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(PspWorkflow.Impl.class, CatchingWorkflow.Impl.class);
        psp = new FlakyPsp();
        worker.registerActivitiesImplementations(psp);
        env.start();
        client = env.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    private PspWorkflow workflow(String id) {
        return client.newWorkflowStub(
                PspWorkflow.class, WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId(id).build());
    }

    private List<EventType> types(String id) {
        return client.fetchHistory(id).getEvents().stream().map(e -> e.getEventType()).toList();
    }

    @Test
    void transientFailuresAreRetriedAndLeaveNoTraceInHistory() {
        psp.failFirst = 2;

        String result = workflow("wf-flaky").run("r-1");

        assertThat(result).isEqualTo("psp-r-1");
        assertThat(psp.calls.get()).isEqualTo(3); // two failures, one success
        assertThat(psp.attemptsSeen).containsExactly(1, 2, 3);
        List<EventType> history = types("wf-flaky");
        assertThat(history.stream().filter(t -> t == EventType.EVENT_TYPE_ACTIVITY_TASK_SCHEDULED)).hasSize(1);
        assertThat(history).doesNotContain(EventType.EVENT_TYPE_ACTIVITY_TASK_FAILED);
    }

    @Test
    void nonRetryableFailureIsNotRetriedAndReachesTheWorkflow() {
        psp.permanentFailure = true;

        assertThatThrownBy(() -> workflow("wf-permanent").run("r-2"))
                .isInstanceOf(WorkflowException.class)
                .cause()
                .isInstanceOf(ActivityFailure.class)
                .cause()
                .isInstanceOfSatisfying(
                        ApplicationFailure.class, f -> assertThat(f.getType()).isEqualTo("CardClosed"));

        assertThat(psp.calls.get()).isEqualTo(1);
        assertThat(types("wf-permanent")).contains(EventType.EVENT_TYPE_ACTIVITY_TASK_FAILED);
    }

    @Test
    void retriesStopAtMaximumAttemptsAndTheFailureSurfaces() {
        psp.failFirst = 100;

        assertThatThrownBy(() -> workflow("wf-exhausted").run("r-3")).isInstanceOf(WorkflowException.class);

        assertThat(psp.calls.get()).isEqualTo(4); // MaximumAttempts
        assertThat(psp.attemptsSeen).containsExactly(1, 2, 3, 4);
    }

    @Test
    void workflowCodeCanCatchTheActivityFailureAndDecide() {
        psp.permanentFailure = true;
        CatchingWorkflow catching = client.newWorkflowStub(
                CatchingWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-caught").build());

        String result = catching.run("r-4");

        assertThat(result).isEqualTo("REFUND_FAILED:CardClosed");
    }

    @WorkflowInterface
    public interface CatchingWorkflow {
        @WorkflowMethod
        String run(String refundId);

        class Impl implements CatchingWorkflow {
            private final PspActivities psp = Workflow.newActivityStub(
                    PspActivities.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());

            @Override
            public String run(String refundId) {
                try {
                    return psp.refund(refundId);
                } catch (ActivityFailure failure) {
                    return "REFUND_FAILED:" + ((ApplicationFailure) failure.getCause()).getType();
                }
            }
        }
    }
}
