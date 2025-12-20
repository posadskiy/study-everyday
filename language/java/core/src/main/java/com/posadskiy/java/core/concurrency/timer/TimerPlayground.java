package com.posadskiy.java.core.concurrency.timer;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Timer;
import java.util.TimerTask;

@Log4j2
public class TimerPlayground {
    @SneakyThrows
    static void main(String[] args) {
        var timer = new Timer();

        timer.schedule(dumbTimerTask("Common"), 1000L);
        timer.schedule(selfCancellableTimerTask("Cancellable"), 500L, 100L);

        // Repeatable running 
        timer.schedule(dumbTimerTask("Repeatable"), 1000L, 1000L);

        timer.scheduleAtFixedRate(dumbTimerTask("Scheduled"), 1000L, 1000L);

        Thread.sleep(20_000);

        timer.cancel();
    }

    public static TimerTask dumbTimerTask(String prefix) {
        return new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(2000);
                log.info("{} Timer task executed", prefix);
            }
        };
    }

    public static TimerTask selfCancellableTimerTask(String prefix) {
        return new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                log.info("{} Timer task executed", prefix);
                this.cancel();
            }
        };
    }
}
