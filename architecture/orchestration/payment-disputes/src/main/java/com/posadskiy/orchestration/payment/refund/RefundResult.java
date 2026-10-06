package com.posadskiy.orchestration.payment.refund;

public record RefundResult(String refundId, RefundStatus status, String pspReference) {

    public enum RefundStatus {
        COMPLETED,
        FAILED
    }
}
