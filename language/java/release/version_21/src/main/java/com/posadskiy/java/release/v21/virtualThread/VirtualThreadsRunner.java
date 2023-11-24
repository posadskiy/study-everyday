package com.posadskiy.java.release.v21.virtualThread;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class VirtualThreadsRunner {
    private final static Logger log = Logger.getLogger("default");

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
                    value.getAndAdd(i*i);
                    return i;
                }));
            log.log(Level.INFO, String.valueOf(value));
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
