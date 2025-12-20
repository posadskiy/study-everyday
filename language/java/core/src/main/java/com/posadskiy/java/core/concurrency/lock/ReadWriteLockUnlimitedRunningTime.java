package com.posadskiy.java.core.concurrency.lock;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

@Log4j2
public class ReadWriteLockUnlimitedRunningTime {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private String data = "Not initialized yes";

    static void main(String[] args) {
        ReadWriteLockUnlimitedRunningTime readWriteLock = new ReadWriteLockUnlimitedRunningTime();
        List<Thread> threads = new ArrayList<>();
        threads.addAll(
            IntStream.range(0, 3).mapToObj(i -> Thread.ofPlatform().daemon().unstarted(readWriteLock::readData)).toList()
        );
        threads.addAll(
            IntStream.range(0, 3).mapToObj(i -> Thread.ofPlatform().daemon().unstarted(() -> readWriteLock.writeData(String.valueOf(i)))).toList()
        );
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void readData() {
        while (true) {
            lock.readLock().lock();

            try {
                Thread.sleep(100);
                log.info("{} read data {}", Thread.currentThread().getName(), data);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    public void writeData(String newData) {
        while (true) {
            lock.writeLock().lock();

            try {
                Thread.sleep(100);
                data = newData;
                log.info("{} write data {}", Thread.currentThread().getName(), newData);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
}
