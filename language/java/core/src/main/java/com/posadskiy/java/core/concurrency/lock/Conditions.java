package com.posadskiy.java.core.concurrency.lock;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class Conditions {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition hasData = lock.newCondition();

    static void main(String[] args) {
        var conditions = new Conditions();
        var threads = List.of(
            Thread.ofPlatform().daemon().start(conditions::reading),
            Thread.ofPlatform().daemon().start(conditions::writing));

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void writing() {
        lock.lock();

        try {
            log.info("writing");
            hasData.signal();
        } finally {
            lock.unlock();
        }
    }

    public void reading() {
        lock.lock();

        try {
            hasData.await();
            log.info("reading");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
