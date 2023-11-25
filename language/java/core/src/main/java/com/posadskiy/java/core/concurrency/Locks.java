package com.posadskiy.java.core.concurrency;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class Locks {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        Runnable runnableOne = () -> {
            log.info("[1] started");
            lock.lock();
            log.info("[1] Want to sleep...");
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[1] Wake up!");
            lock.unlock();
            log.info("[1] Ended");
        };
        Runnable runnableTwo = () -> {
            log.info("[2] started");
            lock.lock();
            log.info("[2] Want to sleep...");
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[2] Wake up!");
            lock.unlock();
            log.info("[2] Ended");
        };

        Thread threadOne = new Thread(runnableOne);
        Thread threadTwo = new Thread(runnableTwo);

        threadOne.start();
        threadTwo.start();

    }
}
