package com.posadskiy.java.core.concurrency.thread;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.stream.IntStream;

@Log4j2
public class JoinOperation {

    @SneakyThrows
    static void main(String[] args) {
        var joinOperation = new JoinOperation();

        joinOperation.simpleExample();
        joinOperation.threadStreamExample();

        log.info("Main thread exit");
    }

    public static Runnable createSleepingRunnable(String details) {
        return () -> {
            log.info("working runnable {}", details);

            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log.info("ending runnable {}", details);
        };
    }

    @SneakyThrows
    public void simpleExample() {
        var thread = Thread.ofPlatform().daemon().start(createSleepingRunnable("first thread"));
        thread.join();
    }

    @SneakyThrows
    public void threadStreamExample() {
        var threads = IntStream.range(0, 10)
            .mapToObj((i) -> Thread.ofPlatform()
                .daemon()
                .start(createSleepingRunnable(String.valueOf(i))))
            .toList();

        threads.forEach((thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
