package com.posadskiy.java.core.concurrency.thread.waitnotify;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WaitNotifyPlayground {
    private static final Object lock = new Object();

    @SneakyThrows
    static void main(String[] args) {

        var firstThread = new Thread(() -> {
            try {
                synchronized (lock) {
                    log.info("First thread :: lock received");
                    lock.wait();
                    log.info("First thread :: notified & completed");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        firstThread.start();

        var secondThread = new Thread(() -> {
            synchronized (lock) {
                log.info("Second thread :: lock received");
                lock.notify();
                log.info("Second thread :: notified & completed");
            }
        });
        secondThread.start();

    }
}
