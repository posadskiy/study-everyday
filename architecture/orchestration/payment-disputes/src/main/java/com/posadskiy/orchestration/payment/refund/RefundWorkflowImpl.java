package com.posadskiy.orchestration.payment.refund;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

import java.time.Duration;

@WorkflowImpl(taskQueues = RefundWorkflowImpl.TASK_QUEUE)
public class RefundWorkflowImpl implements RefundWorkflow {

    public static final String TASK_QUEUE = "refund-task-queue";

    private final RefundActivities activities = Workflow.newActivityStub(
            RefundActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(30))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(5)
                            .setInitialInterval(Duration.ofMillis(200))
                            .setBackoffCoefficient(2.0)
                            .build())
                    .build());

    @Override
    public RefundResult process(RefundRequest request) {
        activities.validate(request);
        String reservationId = activities.reserveFunds(request);
        String pspReference = activities.submitToPsp(request, reservationId);
        activities.postLedger(request, pspReference);
        activities.notifyCustomer(request, pspReference);
        return new RefundResult(request.refundId(), RefundResult.RefundStatus.COMPLETED, pspReference);
    }
}
