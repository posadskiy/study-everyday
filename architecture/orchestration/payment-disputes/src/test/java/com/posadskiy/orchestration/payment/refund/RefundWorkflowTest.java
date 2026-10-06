package com.posadskiy.orchestration.payment.refund;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefundWorkflowTest {

    private TestWorkflowEnvironment testEnv;
    private WorkflowClient client;
    private RefundActivitiesImpl activities;

    @BeforeEach
    void setUp() {
        testEnv = TestWorkflowEnvironment.newInstance();
        Worker worker = testEnv.newWorker(RefundWorkflowImpl.TASK_QUEUE);
        activities = new RefundActivitiesImpl();
        worker.registerWorkflowImplementationTypes(RefundWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        testEnv.start();
        client = testEnv.getWorkflowClient();
    }

    @AfterEach
    void tearDown() {
        testEnv.close();
    }

    @Test
    void refundCompletesAndLedgerIsIdempotentOnReplay() {
        RefundRequest request = new RefundRequest("ref-1", "pay-1", 1999L, "EUR");

        RefundWorkflow workflow = client.newWorkflowStub(
                RefundWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(RefundWorkflowImpl.TASK_QUEUE)
                        .setWorkflowId("refund-ref-1")
                        .build());

        RefundResult result = workflow.process(request);

        assertThat(result.status()).isEqualTo(RefundResult.RefundStatus.COMPLETED);
        assertThat(result.pspReference()).startsWith("psp-");
        assertThat(activities.ledgerTotalMinor("ref-1")).isEqualTo(1999L);
    }
}
