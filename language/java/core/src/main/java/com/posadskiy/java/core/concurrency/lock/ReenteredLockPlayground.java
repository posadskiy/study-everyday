package com.posadskiy.java.core.concurrency.lock;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Log4j2
public class ReenteredLockPlayground {
    private final ReentrantLock lock = new ReentrantLock();

    static void main(String[] args) {
        ReenteredLockPlayground playground = new ReenteredLockPlayground();
        playground.imitateConcurrentAccess();
    }

    public void imitateConcurrentAccess() {
        var features = IntStream.range(0, 5)
            .mapToObj((i) ->
                Thread.ofPlatform()
                    .daemon()
                    .start(() -> doingSomething(i)))
            .toList();

        features.forEach((feature) -> {
            try {
                feature.join();
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
            }
        });
    }

    private void doingSomething(int i) {

        lock.lock();
        try {
            log.info("doing something {}", i);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("doing something interrupted", e);
        } finally {
            lock.unlock();
        }
    }

}
