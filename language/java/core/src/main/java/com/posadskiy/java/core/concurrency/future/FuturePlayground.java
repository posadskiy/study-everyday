package com.posadskiy.java.core.concurrency.future;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j2
public class FuturePlayground {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @SneakyThrows
    static void main(String[] args) {
        var futurePlayground = new FuturePlayground();
        final Future<Integer> calculateFuture = futurePlayground.calculate(10);
        log.info("Calculated result is {}", calculateFuture.get());
        log.info("Calculated result is {}", calculateFuture.get());
        log.info("Calculated result is {}", calculateFuture.get());
    }

    public Future<Integer> calculate(int n) {
        return executorService.submit(() -> {
            Thread.sleep(1000);
            return n * n;
        });
    }
} 
