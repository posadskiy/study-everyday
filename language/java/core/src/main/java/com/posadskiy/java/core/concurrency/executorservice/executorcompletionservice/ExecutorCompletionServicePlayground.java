package com.posadskiy.java.core.concurrency.executorservice.executorcompletionservice;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

public class ExecutorCompletionServicePlayground {
    private final ExecutorCompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(Executors.newSingleThreadExecutor());

    static void main(String[] args) {
        var executorCompletionServicePlayground = new ExecutorCompletionServicePlayground();
        executorCompletionServicePlayground.executorCompletionService.submit(calculationSomething());
        executorCompletionServicePlayground.executorCompletionService.submit(calculationSomething());
        executorCompletionServicePlayground.executorCompletionService.submit(calculationSomething());
        executorCompletionServicePlayground.executorCompletionService.submit(calculationSomething());
    }

    private static Callable<Integer> calculationSomething() {
        return () -> {
            Thread.sleep(1000);
            return (int) (Math.random() * 100);
        };
    }
}
