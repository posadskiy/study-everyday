package com.posadskiy.java.core.concurrency;

import java.util.concurrent.atomic.AtomicLong;

public class Atomics {

    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong(0);
        atomicLong.addAndGet(2);
    }
}
