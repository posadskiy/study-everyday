package com.posadskiy.orchestration.payment.lab11;

import io.temporal.api.enums.v1.EventType;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.client.WorkflowUpdateException;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Lab 6 — signal, query and update on one running workflow.
 *
 * <p>A case waits for evidence (signal), can be inspected at any time (query), and is approved by
 * a reviewer who needs an answer back (update, with a validator).
 */
class Lab06SignalQueryUpdateTest {

    private static final String QUEUE = "sqa-queue";

    @WorkflowInterface
    public interface ReviewWorkflow {

        @WorkflowMethod
        String run(String caseId);

        @SignalMethod
        void addEvidence(String note);

        @QueryMethod
        String stage();

        @QueryMethod
        int evidenceCount();

        @UpdateMethod
        String approve(String reviewer);

        @UpdateValidatorMethod(updateName = "approve")
        void validateApprove(String reviewer);

        class Impl implements ReviewWorkflow {

            private final List<String> evidence = new ArrayList<>();
            private String stage = "STARTED";
            private String approvedBy;

            @Override
            public String run(String caseId) {
                stage = "WAITING_FOR_REVIEW";
                boolean approved = Workflow.await(Duration.ofDays(7), () -> approvedBy != null);
                stage = approved ? "APPROVED" : "EXPIRED";
                return stage;
            }

            @Override
            public void addEvidence(String note) {
                evidence.add(note);
            }

            @Override
            public String stage() {
                return stage;
            }

            @Override
            public int evidenceCount() {
                return evidence.size();
            }

            @Override
            public String approve(String reviewer) {
                approvedBy = reviewer;
                return "approved by " + reviewer + " with " + evidence.size() + " evidence item(s)";
            }

            @Override
            public void validateApprove(String reviewer) {
                if (reviewer == null || reviewer.isBlank()) {
                    throw new IllegalArgumentException("reviewer is required");
                }
                if (approvedBy != null) {
                    throw new IllegalStateException("already approved by " + approvedBy);
                }
            }
        }
    }

    private TestWorkflowEnvironment env;
    private WorkflowClient client;
    private ReviewWorkflow workflow;

    @BeforeEach
    void setUp() {
        env = TestWorkflowEnvironment.newInstance();
        Worker worker = env.newWorker(QUEUE);
        worker.registerWorkflowImplementationTypes(ReviewWorkflow.Impl.class);
        env.start();
        client = env.getWorkflowClient();
        workflow = client.newWorkflowStub(
                ReviewWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(QUEUE).setWorkflowId("wf-review").build());
        WorkflowClient.start(workflow::run, "case-1");
        waitUntilWaiting();
    }

    private void waitUntilWaiting() {
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            try {
                if ("WAITING_FOR_REVIEW".equals(workflow.stage())) {
                    return;
                }
            } catch (RuntimeException notYet) {
                // first workflow task has not run yet; queries need a started workflow
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalStateException("workflow never reached WAITING_FOR_REVIEW");
    }

    @AfterEach
    void tearDown() {
        env.close();
    }

    private List<EventType> historyTypes() {
        return client.fetchHistory("wf-review").getEvents().stream()
                .map(e -> e.getEventType())
                .toList();
    }

    @Test
    void signalChangesStateAndIsRecordedInHistory() {
        workflow.addEvidence("tracking-123");
        workflow.addEvidence("delivery-photo");

        assertThat(workflow.evidenceCount()).isEqualTo(2);
        assertThat(historyTypes().stream().filter(t -> t == EventType.EVENT_TYPE_WORKFLOW_EXECUTION_SIGNALED))
                .hasSize(2);
    }

    @Test
    void queryReadsStateAndLeavesNoTraceInHistory() {
        int eventsBefore = historyTypes().size();

        assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW");
        assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW");

        assertThat(historyTypes()).hasSize(eventsBefore); // two queries, zero new events
    }

    @Test
    void updateReturnsAnAnswerAndIsRecorded() {
        workflow.addEvidence("tracking-123");

        String answer = workflow.approve("alice");

        assertThat(answer).isEqualTo("approved by alice with 1 evidence item(s)");
        assertThat(WorkflowStub.fromTyped(workflow).getResult(String.class)).isEqualTo("APPROVED");
        historyTypes().stream()
                .filter(t -> t.name().contains("UPDATE"))
                .forEach(t -> System.out.println("EVENT " + t));
        assertThat(historyTypes()).contains(EventType.EVENT_TYPE_WORKFLOW_EXECUTION_UPDATE_ACCEPTED);
    }

    @Test
    void validatorRejectsABadUpdateAndRecordsNoUpdateEvent() {
        int eventsBefore = historyTypes().size();

        assertThatThrownBy(() -> workflow.approve(" ")).isInstanceOf(WorkflowUpdateException.class);

        assertThat(workflow.stage()).isEqualTo("WAITING_FOR_REVIEW"); // state untouched
        // The validator runs inside a workflow task, so empty WORKFLOW_TASK_* events may appear,
        // but no UPDATE_* event, signal or timer change is recorded.
        assertThat(historyTypes().stream().filter(t -> t.name().contains("UPDATE"))).isEmpty();
        assertThat(historyTypes().size()).isGreaterThanOrEqualTo(eventsBefore);
    }

    @Test
    void updateAfterTheWorkflowFinishedIsRefused() {
        workflow.approve("alice");
        WorkflowStub.fromTyped(workflow).getResult(String.class);

        assertThatThrownBy(() -> workflow.approve("bob")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void queryStillWorksAfterTheWorkflowHasFinished() {
        workflow.approve("alice");
        WorkflowStub.fromTyped(workflow).getResult(String.class);

        assertThat(workflow.stage()).isEqualTo("APPROVED");
    }

    @Test
    void ifNobodyApprovesTheTimerWins() {
        String result = WorkflowStub.fromTyped(workflow).getResult(String.class);

        assertThat(result).isEqualTo("EXPIRED");
    }
}
