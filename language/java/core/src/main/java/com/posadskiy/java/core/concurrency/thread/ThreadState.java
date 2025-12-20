package com.posadskiy.java.core.concurrency.thread;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ThreadState {
    static void main(String[] args) {
        var threadState = new ThreadState();

        threadState.playingWithCurrentThread();
        threadState.creatingCommonThread();
        threadState.creatingVirtualThread();
    }

    public void playingWithCurrentThread() {
        var currentThread = Thread.currentThread();
        printState(currentThread);
    }

    public void creatingCommonThread() {
        log.info("creatingCommonThread :: start");

        var thread = Thread.ofPlatform().daemon().unstarted(createRunnable("common thread"));
        printState(thread);
        log.info("creatingCommonThread :: starting thread");
        thread.start();
        printState(thread);
        log.info("creatingCommonThread :: end");

        printState(thread);
    }

    public void creatingVirtualThread() {
        log.info("creatingVirtualThread :: start");

        var thread = Thread.ofVirtual().unstarted(createRunnable("virtual thread"));
        printState(thread);
        log.info("creatingVirtualThread :: starting thread");
        thread.start();
        printState(thread);

        log.info("creatingVirtualThread :: end");
    }

    public Runnable createRunnable(String details) {
        return () -> log.info("working runnable {}", details);
    }

    public void printState(Thread thread) {
        log.info("Thread {}, priority {}, ID {}, state {}", thread.getName(), thread.getPriority(), thread.threadId(), thread.getState());
    }
}
