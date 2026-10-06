package com.posadskiy.orchestration.payment.refund;

import java.util.Objects;

/** Amount in minor units (cents); currency ISO-4217. */
public record RefundRequest(String refundId, String paymentId, long amountMinor, String currency) {

    public RefundRequest {
        Objects.requireNonNull(refundId, "refundId");
        Objects.requireNonNull(paymentId, "paymentId");
        Objects.requireNonNull(currency, "currency");
        if (amountMinor <= 0) {
            throw new IllegalArgumentException("amountMinor must be positive");
        }
    }
}
