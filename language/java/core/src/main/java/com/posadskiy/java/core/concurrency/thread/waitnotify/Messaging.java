package com.posadskiy.java.core.concurrency.thread.waitnotify;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Messaging {
    public static final Object lock = new Object();
    public static final Queue<String> queue = new LinkedList<>();

    static void main(String[] args) {
        IntStream.range(0, 5).forEach((i) -> new Thread(new Receiver(String.valueOf(i))).start());
        new Thread(new Sender()).start();
    }
}

@Log4j2
class Sender implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            var element = String.valueOf(ThreadLocalRandom.current().nextInt());
            synchronized (Messaging.lock) {
                Messaging.queue.offer(element);
                Messaging.lock.notify();
            }
            log.info("Sender :: {}", element);
            Thread.sleep(1000);
        }
    }
}

@Log4j2
@AllArgsConstructor
class Receiver implements Runnable {
    private String id;

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            synchronized (Messaging.lock) {
                while (Messaging.queue.isEmpty()) {
                    Messaging.lock.wait();
                }
                log.info("Receiver {} :: {}", this.id, Messaging.queue.poll());
            }

            Thread.sleep(1000);
        }
    }
}
