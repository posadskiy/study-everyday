package com.posadskiy.java.core.concurrency.executorservice;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Log4j2
public class ExecutorServicePlayground {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    static void main(String[] args) {
        IntStream.range(0, 10).forEach(i -> executorService.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("Printing some logs");
        }));

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
