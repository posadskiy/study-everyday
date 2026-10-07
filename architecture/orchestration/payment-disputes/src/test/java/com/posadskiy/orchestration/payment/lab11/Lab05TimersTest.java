package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lab 5 — durable timers. A 30-day wait costs no thread, and the test server can skip time.
 */
class Lab05TimersTest {

    private static final String QUEUE = "timer-queue";

    @WorkflowInterface
    public interface DeadlineWorkflow {

        @WorkflowMethod
        String run(String chargebackId);

        @SignalMethod
        void evidenceReceived();

        class Impl implements DeadlineWorkflow {

            private final StepActivities activities = Workflow.newActivityStub(
                    StepActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .build());

            private boolean evidence;

            @Override
            public String run(String chargebackId) {
                // Block until evidence arrives OR 30 days pass, whichever is first.
                boolean arrived = Workflow.await(Duration.ofDays(30), () -> evidence);
                if (arrived) {
                    activities.step("represent");
                    return "REPRESENTED";
                }
                activities.step("accept-liability");
                return "ACCEPTED_AFTER_DEADLINE";
            }

            @Override
            public void evidenceReceived() {
                evidence = true;
            }
        }
    }

    @WorkflowInterface
    public interface CoolingOffWorkflow {

        @WorkflowMethod
        String run(String refundId);

        class Impl implements CoolingOffWorkflow {

            private final StepActivities activities = Workflow.newActivityStub(
                    StepActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .build());

            @Override
            public String run(String refundId) {
                activities.step("reserve");
                Workflow.sleep(Duration.ofHours(24)); // durable: no thread is held for a day
                activities.step("release");
                return "released";
            }
        }
    }

    private TestWorkflowEnvironment env;
    private WorkflowClient client;
    private RecordingStepActivities activities;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(DeadlineWorkflow.Impl.class, CoolingOffWorkflow.Impl.class);
        activities = new RecordingStepActivities();
        worker.registerActivitiesImplementations(activities);
        env.start();
        client = env.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    private DeadlineWorkflow newWorkflow(String id) {
        return client.newWorkflowStub(
                DeadlineWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId(id).build());
    }

    @Test
    void thirtyDayDeadlinePassesInMillisecondsOfRealTime() {
        long wallStart = System.nanoTime();
        long testTimeStart = env.currentTimeMillis();

        String result = newWorkflow("wf-deadline").run("cb-1");

        long testDays = Duration.ofMillis(env.currentTimeMillis() - testTimeStart).toDays();
        long wallMillis = Duration.ofNanos(System.nanoTime() - wallStart).toMillis();
        System.out.println("TEST TIME ELAPSED: " + testDays + " days; WALL TIME: " + wallMillis + " ms");

        assertThat(result).isEqualTo("ACCEPTED_AFTER_DEADLINE");
        assertThat(activities.calls).containsExactly("accept-liability");
        assertThat(testDays).isGreaterThanOrEqualTo(30);
        assertThat(wallMillis).isLessThan(10_000);
    }

    @Test
    void evidenceBeforeTheDeadlineWinsTheRaceAndTheTimerNeverFires() {
        DeadlineWorkflow workflow = newWorkflow("wf-evidence");
        WorkflowClient.start(workflow::run, "cb-2");

        env.sleep(Duration.ofDays(10)); // ten days pass with no evidence
        workflow.evidenceReceived();

        String result = io.temporal.client.WorkflowStub.fromTyped(workflow).getResult(String.class);
        assertThat(result).isEqualTo("REPRESENTED");

        var types = client.fetchHistory("wf-evidence").getEvents().stream()
                .map(e -> e.getEventType())
                .toList();
        types.forEach(t -> System.out.println("EVENT " + t));
        assertThat(types).contains(EventType.EVENT_TYPE_TIMER_STARTED);
        assertThat(types).doesNotContain(EventType.EVENT_TYPE_TIMER_FIRED);
    }

    @Test
    void sleepWritesTimerStartedThenTimerFiredBetweenTwoActivities() {
        CoolingOffWorkflow workflow = client.newWorkflowStub(
                CoolingOffWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-cooling").build());

        assertThat(workflow.run("r-1")).isEqualTo("released");

        var types = client.fetchHistory("wf-cooling").getEvents().stream()
                .map(e -> e.getEventType())
                .filter(t -> t.name().contains("TIMER") || t.name().contains("ACTIVITY_TASK_SCHEDULED"))
                .toList();
        types.forEach(t -> System.out.println("EVENT " + t));
        assertThat(types).containsExactly(
                EventType.EVENT_TYPE_ACTIVITY_TASK_SCHEDULED,
                EventType.EVENT_TYPE_TIMER_STARTED,
                EventType.EVENT_TYPE_TIMER_FIRED,
                EventType.EVENT_TYPE_ACTIVITY_TASK_SCHEDULED);
    }
}
