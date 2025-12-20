package com.posadskiy.java.core.concurrency.thread;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class InterruptState {
    public static final Object LOCK = new Object();

    static void main(String[] args) {
        var thread = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    log.error("Interrupted exception: {}", e.getLocalizedMessage());
                }
            }
        });
        thread.start();
        thread.interrupt();
    }
}
