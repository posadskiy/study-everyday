package com.posadskiy.java.core.concurrency.lock;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

@Log4j2
public class StampedLockPlayground {
    private final StampedLock lock = new StampedLock();
    private String data = "Not initialized yet";

    public static void main(String[] args) {
        StampedLockPlayground stampedLockPlayground = new StampedLockPlayground();
        List<Thread> threads = new ArrayList<>();
        threads.addAll(
            IntStream.range(0, 3).mapToObj(i -> Thread.ofPlatform().daemon().unstarted(stampedLockPlayground::readData)).toList()
        );
        threads.add(Thread.ofPlatform().daemon().unstarted(stampedLockPlayground::writeData));
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SneakyThrows
    public void readData() {
        while (true) {
            var stamp = lock.tryReadLock();
            if (!lock.validate(stamp)) {
                log.info("{} :: Read lock failed", Thread.currentThread().getName());
                continue;
            }

            try {
                log.info("{} :: read data {}", Thread.currentThread().getName(), data);
            } finally {
                if (lock.validate(stamp)) {
                    lock.unlockRead(stamp);
                }
            }

            Thread.sleep(1000);
        }
    }

    @SneakyThrows
    public void writeData() {
        while (true) {
            var stamp = lock.writeLock();
            if (stamp == 0) {
                log.info("{} :: lock is not available", Thread.currentThread().getName());
                continue;
            }

            try {
                data = RandomStringUtils.randomAlphabetic(10);
                log.info("{} :: write data {}", Thread.currentThread().getName(), data);
            } finally {
                lock.unlockWrite(stamp);
            }

            Thread.sleep(5000);
        }
    }
}
