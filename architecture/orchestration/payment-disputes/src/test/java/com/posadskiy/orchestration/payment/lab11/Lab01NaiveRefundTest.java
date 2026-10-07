package com.posadskiy.orchestration.payment.lab11;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Lab 1 — the problem durable execution solves.
 *
 * <p>A refund written as plain Java, with the process "crashing" between two steps. No framework
 * involved: the point is to see the failure before learning the cure.
 */
class Lab01NaiveRefundTest {

    /** Everything a naive refund touches, recorded so the test can inspect it. */
    static class World {
        final List<String> pspRefunds = new ArrayList<>();
        final List<String> ledgerEntries = new ArrayList<>();
        boolean crashAfterPsp;
    }

    static class NaiveRefundService {
        private final World world;

        NaiveRefundService(World world) {
            this.world = world;
        }

        void refund(String refundId, long amountMinor) {
            world.pspRefunds.add(refundId + ":" + amountMinor); // step 1: money leaves
            if (world.crashAfterPsp) {
                throw new IllegalStateException("process killed");
            }
            world.ledgerEntries.add(refundId + ":" + amountMinor); // step 2: we record it
        }
    }

    @Test
    void crashBetweenStepsLeavesMoneyMovedButNotRecorded() {
        World world = new World();
        world.crashAfterPsp = true;
        NaiveRefundService service = new NaiveRefundService(world);

        assertThatThrownBy(() -> service.refund("ref-1", 1999)).isInstanceOf(IllegalStateException.class);

        assertThat(world.pspRefunds).hasSize(1); // customer was refunded
        assertThat(world.ledgerEntries).isEmpty(); // our books say nothing happened
    }

    @Test
    void naiveRetryAfterCrashRefundsTwice() {
        World world = new World();
        NaiveRefundService service = new NaiveRefundService(world);

        world.crashAfterPsp = true;
        assertThatThrownBy(() -> service.refund("ref-1", 1999)).isInstanceOf(IllegalStateException.class);

        // The process restarts and a scheduler "just retries" the whole refund.
        world.crashAfterPsp = false;
        service.refund("ref-1", 1999);

        assertThat(world.pspRefunds).hasSize(2); // customer refunded TWICE
        assertThat(world.ledgerEntries).hasSize(1); // books record once
    }
}
