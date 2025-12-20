package com.posadskiy.java.core.concurrency.thread;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/// The main thread is waiting for created one to be completed, so its state is WAITING
@Log4j2
public class WaitingState {
    public static Thread currentThread = Thread.currentThread();

    @SneakyThrows
    static void main(String[] args) {
        var thread = new Thread(() -> {
            log.info(WaitingState.currentThread.getState());
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        thread.join();
    }
}
