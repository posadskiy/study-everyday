package com.posadskiy.java.core.concurrency.future;

import lombok.extern.log4j.Log4j2;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

///  The goal is to process large datasets in parallel, aggregate the results, and manage concurrency effectively.
@State(Scope.Benchmark)
@Log4j2
public class DatasetProcessing {
    private final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private final int DATASET_SIZE = 10_000_000;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private final int[] dataset = createDataset();

    private int[] createDataset() {
        return IntStream.range(0, DATASET_SIZE).map((i) -> (int) (Math.random() * 100)).toArray();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 2)
    public void measureSumParallel() {
        sumParallel();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 2)
    public void measureSum() {
        //noinspection ResultOfMethodCallIgnored
        sum(dataset);
    }

    private long sumParallel() {
        var futures = IntStream.range(0, THREAD_COUNT).mapToObj(i -> executorService.submit(() -> {
            var start = i * DATASET_SIZE / THREAD_COUNT;
            var end = (i + 1) * DATASET_SIZE / THREAD_COUNT;
            var sum = 0L;
            for (int j = start; j < end; j++) {
                sum += dataset[j];
            }

            return sum;
        })).toList();

        AtomicLong finalSum = new AtomicLong();
        futures.forEach(future -> {
            try {
                finalSum.addAndGet(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        return finalSum.get();
    }

    private long sum(int[] dataset) {
        var sum = 0L;
        for (int j = 0; j < DATASET_SIZE; j++) {
            sum += dataset[j];
        }

        return sum;
    }
}
