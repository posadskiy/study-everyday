package com.posadskiy.java.core.concurrency.lock;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Log4j2
public class ReenteredLockPlayground {
    private final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ReenteredLockPlayground playground = new ReenteredLockPlayground();
        
        var features = IntStream.range(0, 5).mapToObj((i) -> Thread.ofPlatform().daemon().start(playground::doingSomething)).toList();
        features.forEach((feature) -> {
            try {
                feature.join();
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
            }
        });
    }
    
    private void doingSomething() {
        lock.lock();
        
        try {
            log.info("doing something");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("doing something interrupted", e);
        } finally {
            lock.unlock();
        }
    }
    
    
}
