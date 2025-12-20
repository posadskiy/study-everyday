package com.posadskiy.java.core.concurrency.executorservice.scheduledexecutorservice;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Executors;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ScheduledExecutorServicePlayground {
    private final static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);

    @SneakyThrows
    static void main(String[] args) {
        ScheduledExecutorServicePlayground.scheduledExecutorService.schedule(dumbRunnableWithSleeping("schedule"), 1000L, TimeUnit.MILLISECONDS);

        var scheduleWithFixedDelayTask = ScheduledExecutorServicePlayground.scheduledExecutorService.scheduleWithFixedDelay(dumbRunnableWithSleeping("scheduleWithFixedDelay"), 1000L, 1000L, TimeUnit.MILLISECONDS);

        var scheduledAtFixedRateTask = ScheduledExecutorServicePlayground.scheduledExecutorService.scheduleAtFixedRate(dumbRunnableWithSleeping("scheduleAtFixedRate"), 1000L, 1000L, TimeUnit.MILLISECONDS);

        if (scheduledAtFixedRateTask instanceof RunnableScheduledFuture<?> runnableScheduledFuture) {
            log.info("scheduleWithFixedDelay is periodic: {}", runnableScheduledFuture.isPeriodic());
            log.info("scheduleWithFixedDelay current delay: {}", runnableScheduledFuture.getDelay(TimeUnit.MILLISECONDS));
        }

        Thread.sleep(10_000);

        log.info("Shutdown is going to be executed");
        scheduledExecutorService.shutdown();

        log.info("Both task are going to be cancelled");
        scheduleWithFixedDelayTask.cancel(false);
        scheduledAtFixedRateTask.cancel(false);

        log.info("Awaiting termination is {}", scheduledExecutorService.awaitTermination(10, TimeUnit.MILLISECONDS));

        scheduledExecutorService.shutdownNow();

        log.info("Awaiting termination is {}", scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS));
    }

    public static Runnable dumbRunnableWithSleeping(String prefix) {
        return () -> {
            log.info("{} :: Timer task started", prefix);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted", e);
            }

            log.info("{} :: Timer task completed", prefix);
        };
    }
}
