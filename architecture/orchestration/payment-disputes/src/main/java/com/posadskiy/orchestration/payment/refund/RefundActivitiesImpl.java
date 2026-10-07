package com.posadskiy.orchestration.payment.refund;

import io.temporal.spring.boot.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ActivityImpl(taskQueues = RefundWorkflowImpl.TASK_QUEUE)
public class RefundActivitiesImpl implements RefundActivities {

    private static final Logger log = LoggerFactory.getLogger(RefundActivitiesImpl.class);

    /** Simulated PSP: idempotent on refundId. */
    private final Map<String, String> pspByRefundId = new ConcurrentHashMap<>();
    private final Map<String, Long> ledgerMinorByRefundId = new ConcurrentHashMap<>();

    /** Lab hook: makes the PSP call slow so there is time to kill the worker mid-activity. */
    @Value("${refund.psp-delay:PT0S}")
    private Duration pspDelay = Duration.ZERO;

    @Override
    public void validate(RefundRequest request) {
        log.info("validate refundId={} paymentId={}", request.refundId(), request.paymentId());
    }

    @Override
    public String reserveFunds(RefundRequest request) {
        String reservationId = "res-" + request.refundId();
        log.info("reserveFunds refundId={} reservationId={}", request.refundId(), reservationId);
        return reservationId;
    }

    @Override
    public String submitToPsp(RefundRequest request, String reservationId) {
        return pspByRefundId.computeIfAbsent(request.refundId(), id -> {
            log.info("submitToPsp refundId={} reservationId={}", id, reservationId);
            sleep(pspDelay);
            return "psp-" + UUID.randomUUID();
        });
    }

    @Override
    public void postLedger(RefundRequest request, String pspReference) {
        ledgerMinorByRefundId.merge(request.refundId(), request.amountMinor(), Long::sum);
        log.info(
                "postLedger refundId={} pspReference={} totalMinor={}",
                request.refundId(),
                pspReference,
                ledgerMinorByRefundId.get(request.refundId()));
    }

    @Override
    public void notifyCustomer(RefundRequest request, String pspReference) {
        log.info("notifyCustomer refundId={} pspReference={}", request.refundId(), pspReference);
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while waiting on the PSP", e);
        }
    }

    /** Test hook: how many times ledger was credited for this refund. */
    long ledgerTotalMinor(String refundId) {
        return ledgerMinorByRefundId.getOrDefault(refundId, 0L);
    }
}
