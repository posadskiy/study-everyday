package com.posadskiy.java.core.concurrency.thread;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/// Two threads in the example below, one of them is waiting 10 seconds for another completion.
/// As a result, one thread is in BLOCKED state, while another is running
@Log4j2
public class BlockedState {
    @SneakyThrows
    static void main(String[] args) {
        var threadOne = Thread.ofPlatform().daemon().start(createRunnable("first thread"));
        var threadTwo = Thread.ofPlatform().daemon().start(createRunnable("second thread"));

        Thread.sleep(1_000);

        log.info(threadOne.getState());
        log.info(threadTwo.getState());

        Thread.sleep(10_000);

        log.info(threadOne.getState());
        log.info(threadTwo.getState());

        log.info("waiting for second thread to be completed");
        Thread.sleep(10_000);
    }

    public static Runnable createRunnable(String details) {
        return () -> {
            log.info("start runnable {}", details);
            synchronized (BlockedState.class) {
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("end runnable {}", details);
        };
    }
}
