package com.posadskiy.java.release.v21.virtualThread;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Log4j2
public class VirtualThreadsRunner {

    public static void main(String[] args) {
        var runner = new VirtualThreadsRunner();
        runner.runSomeVirtualThreads();
        runner.waitOnSomething();
    }

    public void runSomeVirtualThreads() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            AtomicLong value = new AtomicLong();
            IntStream.range(0, 10000)
                .forEach(i -> executor.submit(() -> {
                    Thread.sleep(Duration.ofMillis(100));
                    value.getAndAdd(i * i);
                    return i;
                }));
            log.info(String.valueOf(value));
        }
    }

    public void waitOnSomething() {
        final Object lock = new Object();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> {
                synchronized (lock) {
                    try {
                        Thread.sleep(Duration.ofSeconds(10));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }
}
