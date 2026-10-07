package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.EventType;
import io.temporal.api.history.v1.HistoryEvent;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.WorkflowExecutionHistory;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.WorkflowReplayer;
import io.temporal.worker.Worker;
import io.temporal.workflow.Workflow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Lab 2 — event history and replay.
 *
 * <p>Runs a three-step workflow, reads its recorded history, then replays that history against
 * workflow code to see what the engine does after a crash.
 */
class Lab02HistoryAndReplayTest {

    private static final String QUEUE = "lab-queue";

    private TestWorkflowEnvironment env;
    private WorkflowClient client;
    private RecordingStepActivities activities;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(ThreeStepWorkflow.Impl.class);
        activities = new RecordingStepActivities();
        worker.registerActivitiesImplementations(activities);
        env.start();
        client = env.getWorkflowClient();
        ThreeStepWorkflow.BODY_RUNS.set(0);
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    private WorkflowExecutionHistory runOnce(String workflowId) {
        ThreeStepWorkflow workflow = client.newWorkflowStub(
                ThreeStepWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId(workflowId).build());
        workflow.run("ref-1");
        return client.fetchHistory(workflowId);
    }

    @Test
    void historyIsAnOrderedRecordOfEverythingThatHappened() {
        WorkflowExecutionHistory history = runOnce("wf-history");

        List<EventType> types = history.getEvents().stream().map(HistoryEvent::getEventType).toList();
        types.forEach(t -> System.out.println("EVENT " + t));

        assertThat(types).first().isEqualTo(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_STARTED);
        assertThat(types).last().isEqualTo(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_COMPLETED);
        assertThat(types.stream().filter(t -> t == EventType.EVENT_TYPE_ACTIVITY_TASK_SCHEDULED))
                .hasSize(3);
        assertThat(types.stream().filter(t -> t == EventType.EVENT_TYPE_ACTIVITY_TASK_COMPLETED))
                .hasSize(3);
    }

    @Test
    void replayingARecordedHistoryRerunsTheCodeButNotTheActivities() throws Exception {
        WorkflowExecutionHistory history = runOnce("wf-replay");
        int activityCallsBefore = activities.calls.size();
        int bodyRunsBefore = ThreeStepWorkflow.BODY_RUNS.get();

        WorkflowReplayer.replayWorkflowExecution(history, ThreeStepWorkflow.Impl.class);

        assertThat(activities.calls).hasSize(activityCallsBefore); // no activity ran again
        assertThat(ThreeStepWorkflow.BODY_RUNS.get()).isGreaterThan(bodyRunsBefore); // the code did
    }

    @Test
    void replayAgainstCodeThatCallsADifferentActivityFailsWithNonDeterminism() {
        WorkflowExecutionHistory history = runOnce("wf-nondeterministic");

        assertThatThrownBy(() -> WorkflowReplayer.replayWorkflowExecution(history, DifferentActivityImpl.class))
                .hasMessageContaining("NonDeterministicException")
                .hasMessageContaining("doesn't match event");
    }

    @Test
    void swappingArgumentsOfTheSameActivityIsNotDetected() throws Exception {
        WorkflowExecutionHistory history = runOnce("wf-swapped-arguments");

        // Replay compares the kind of command and the activity type — not the arguments. This
        // reordering passes silently, and the recorded results are fed to the wrong calls.
        WorkflowReplayer.replayWorkflowExecution(history, SwappedArgumentsImpl.class);
    }

    /** Second step is now a different activity type (audit instead of step). */
    public static class DifferentActivityImpl implements ThreeStepWorkflow {

        private final StepActivities activities = stub();

        @Override
        public String run(String refundId) {
            activities.step("validate");
            activities.audit("reserve");
            activities.step("psp");
            return "done:" + refundId;
        }
    }

    /** Same activity type, but "psp" and "reserve" swapped. */
    public static class SwappedArgumentsImpl implements ThreeStepWorkflow {

        private final StepActivities activities = stub();

        @Override
        public String run(String refundId) {
            activities.step("validate");
            activities.step("psp");
            activities.step("reserve");
            return "done:" + refundId;
        }
    }

    private static StepActivities stub() {
        return Workflow.newActivityStub(
                StepActivities.class,
                ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());
    }
}
