package com.posadskiy.orchestration.payment.lab11;

import io.temporal.activity.ActivityInterface;

/** Minimal activities for the labs: each call is recorded so tests can count executions. */
@ActivityInterface
public interface StepActivities {

    void step(String name);

    void audit(String name);
}
