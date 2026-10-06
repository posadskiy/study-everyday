package com.posadskiy.orchestration.payment.refund;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface RefundWorkflow {

    @WorkflowMethod
    RefundResult process(RefundRequest request);
}
