package com.posadskiy.orchestration.payment.refund;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface RefundActivities {

    @ActivityMethod
    void validate(RefundRequest request);

    @ActivityMethod
    String reserveFunds(RefundRequest request);

    @ActivityMethod
    String submitToPsp(RefundRequest request, String reservationId);

    @ActivityMethod
    void postLedger(RefundRequest request, String pspReference);

    @ActivityMethod
    void notifyCustomer(RefundRequest request, String pspReference);
}
