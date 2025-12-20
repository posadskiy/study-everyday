package com.posadskiy.java.core.concurrency.thread;

import lombok.extern.log4j.Log4j2;

import java.util.stream.IntStream;

/// Virtual threads are lightweight threads that reduce the effort of writing, maintaining, and debugging high-throughput
/// concurrent applications.
///
/// Virtual threads typically employ a small set of platform threads used as carrier threads. Locking and I/ O operations
/// are examples of operations where a carrier thread may be re-scheduled from one virtual thread to another.
@Log4j2
public class VirtualThread {
    static void main(String[] args) {
        var virtualThread = new VirtualThread();

        // 300 ms on quick test
        virtualThread.createMillionVirtualThreads();

        // more than 20 s on quick test
        virtualThread.createMillionPlatformThreads();
    }

    public void createMillionVirtualThreads() {
        IntStream.range(0, 1_000_000).forEach((i) -> Thread.ofVirtual().start(createRunnable(String.valueOf(i))));
    }

    public void createMillionPlatformThreads() {
        IntStream.range(0, 1_000_000).forEach((i) -> Thread.ofPlatform().start(createRunnable(String.valueOf(i))));
    }

    public Runnable createRunnable(String details) {
        return () -> log.info("working runnable {}", details);
    }
}
