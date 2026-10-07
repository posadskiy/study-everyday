package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A three-step workflow whose body counts how many times it starts executing.
 *
 * <p>The static counter is deliberately the kind of thing you must NOT do in real workflow code
 * (lesson 1.4). Here it is a read-only probe: it never influences a decision.
 */
@WorkflowInterface
public interface ThreeStepWorkflow {

    AtomicInteger BODY_RUNS = new AtomicInteger();

    @WorkflowMethod
    String run(String refundId);

    class Impl implements ThreeStepWorkflow {

        private final StepActivities activities = Workflow.newActivityStub(
                StepActivities.class,
                ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(10)).build());

        @Override
        public String run(String refundId) {
            BODY_RUNS.incrementAndGet();
            activities.step("validate");
            activities.step("reserve");
            activities.step("psp");
            return "done:" + refundId;
        }
    }
}
