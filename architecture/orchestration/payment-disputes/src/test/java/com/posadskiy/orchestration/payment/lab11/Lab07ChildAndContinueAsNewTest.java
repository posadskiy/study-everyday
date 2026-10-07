package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Lab 7 — child workflows (fan-out) and continue-as-new (bounded history).
 */
class Lab07ChildAndContinueAsNewTest {

    private static final String QUEUE = "child-queue";

    @WorkflowInterface
    public interface EvidenceItemWorkflow {

        @WorkflowMethod
        String collect(int index);

        class Impl implements EvidenceItemWorkflow {
            @Override
            public String collect(int index) {
                return "item-" + index;
            }
        }
    }

    @WorkflowInterface
    public interface CollectAllWorkflow {

        @WorkflowMethod
        String run(int count);

        class Impl implements CollectAllWorkflow {
            @Override
            public String run(int count) {
                List<Promise<String>> pending = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    EvidenceItemWorkflow child = Workflow.newChildWorkflowStub(
                            EvidenceItemWorkflow.class,
                            ChildWorkflowOptions.newBuilder().setWorkflowId("evidence-item-" + i).build());
                    pending.add(Async.function(child::collect, i)); // all children run in parallel
                }
                Promise.allOf(pending).get();
                return pending.stream().map(Promise::get).collect(Collectors.joining(","));
            }
        }
    }

    @WorkflowInterface
    public interface BatchWorkflow {

        int BATCH = 3;

        @WorkflowMethod
        String run(int from, int total);

        class Impl implements BatchWorkflow {

            private final StepActivities activities = Workflow.newActivityStub(
                    StepActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .build());

            @Override
            public String run(int from, int total) {
                int end = Math.min(from + BATCH, total);
                for (int i = from; i < end; i++) {
                    activities.step("payout-" + i);
                }
                if (end < total) {
                    // Finish this run and start a fresh one (same workflow ID, empty history).
                    Workflow.continueAsNew(end, total);
                }
                return "all " + total + " payouts done";
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
        worker.registerWorkflowImplementationTypes(
                EvidenceItemWorkflow.Impl.class, CollectAllWorkflow.Impl.class, BatchWorkflow.Impl.class);
        activities = new RecordingStepActivities();
        worker.registerActivitiesImplementations(activities);
        env.start();
        client = env.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    @Test
    void parentFansOutToChildrenThatEachHaveTheirOwnHistory() {
        CollectAllWorkflow parent = client.newWorkflowStub(
                CollectAllWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-parent").build());

        String result = parent.run(3);

        assertThat(result).isEqualTo("item-0,item-1,item-2");
        var parentTypes = client.fetchHistory("wf-parent").getEvents().stream()
                .map(e -> e.getEventType())
                .toList();
        assertThat(parentTypes.stream().filter(t -> t == EventType.EVENT_TYPE_CHILD_WORKFLOW_EXECUTION_STARTED))
                .hasSize(3);
        // Each child is a separate execution with its own history.
        var childTypes = client.fetchHistory("evidence-item-1").getEvents().stream()
                .map(e -> e.getEventType())
                .toList();
        assertThat(childTypes).first().isEqualTo(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_STARTED);
        assertThat(childTypes).last().isEqualTo(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_COMPLETED);
    }

    @Test
    void continueAsNewSplitsALongJobIntoRunsWithShortHistories() {
        BatchWorkflow workflow = client.newWorkflowStub(
                BatchWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-batch").build());

        var execution = WorkflowClient.start(workflow::run, 0, 8);
        // getResult follows the chain of runs by default and returns the last run's result.
        String result = WorkflowStub.fromTyped(workflow).getResult(String.class);

        assertThat(result).isEqualTo("all 8 payouts done");
        assertThat(activities.calls).hasSize(8).first().isEqualTo("payout-0");
        assertThat(activities.calls).last().isEqualTo("payout-7");

        var firstRun = client.fetchHistory("wf-batch", execution.getRunId()).getEvents();
        System.out.println("FIRST RUN: " + firstRun.size() + " events, last = "
                + firstRun.get(firstRun.size() - 1).getEventType());
        assertThat(firstRun.get(firstRun.size() - 1).getEventType())
                .isEqualTo(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_CONTINUED_AS_NEW);
    }
}
