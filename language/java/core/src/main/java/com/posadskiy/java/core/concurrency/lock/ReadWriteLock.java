package com.posadskiy.java.core.concurrency.lock;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

@Log4j2
public class ReadWriteLock {
   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   private String data = "Not initialized yes";

    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReadWriteLock();
        List<Thread> threads = new ArrayList<>();
        threads.addAll(
            IntStream.range(0, 10).mapToObj(i -> Thread.ofPlatform().daemon().unstarted(readWriteLock::readData)).toList()
        );
        threads.addAll(
            IntStream.range(0, 20).mapToObj(i -> Thread.ofPlatform().daemon().unstarted(() -> readWriteLock.writeData(String.valueOf(i)))).toList()
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
        lock.readLock().lock();
        
        try {
            log.info("{} read data {}", Thread.currentThread().getName(), data);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void writeData(String newData) {
        lock.writeLock().lock();
        
        try {
            data = newData;
            log.info("{} write data {}", Thread.currentThread().getName(), newData);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
