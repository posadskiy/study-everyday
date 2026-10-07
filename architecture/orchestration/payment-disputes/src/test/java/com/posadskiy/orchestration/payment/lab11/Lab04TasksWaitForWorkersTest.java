package com.posadskiy.orchestration.payment.lab11;

import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lab 4 — the server never runs your code, so with no worker nothing happens, and nothing is lost.
 */
class Lab04TasksWaitForWorkersTest {

    private static final String QUEUE = "wait-queue";

    private TestWorkflowEnvironment env;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    @Test
    void workflowStartedBeforeAnyWorkerExistsRunsWhenAWorkerAppears() throws Exception {
        WorkflowClient client = env.getWorkflowClient();
        RecordingStepActivities activities = new RecordingStepActivities();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(ThreeStepWorkflow.Impl.class);
        worker.registerActivitiesImplementations(activities);

        // No worker is polling yet (env.start() has not been called).
        ThreeStepWorkflow workflow = client.newWorkflowStub(
                ThreeStepWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-early").build());
        WorkflowClient.start(workflow::run, "ref-1");

        var eventsBeforeWorker = client.fetchHistory("wf-early").getEvents();
        eventsBeforeWorker.forEach(e -> System.out.println("BEFORE WORKER " + e.getEventType()));
        assertThat(eventsBeforeWorker.stream().map(e -> e.getEventType()).toList())
                .containsExactly(
                        EventType.EVENT_TYPE_WORKFLOW_EXECUTION_STARTED,
                        EventType.EVENT_TYPE_WORKFLOW_TASK_SCHEDULED); // queued, not started
        assertThat(activities.calls).isEmpty();

        env.start(); // a worker begins polling the queue

        String result = io.temporal.client.WorkflowStub.fromTyped(workflow)
                .getResult(30, TimeUnit.SECONDS, String.class);
        assertThat(result).isEqualTo("done:ref-1");
        assertThat(activities.calls).containsExactly("validate", "reserve", "psp");
    }
}
