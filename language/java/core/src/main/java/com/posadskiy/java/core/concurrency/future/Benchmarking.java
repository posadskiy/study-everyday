package com.posadskiy.java.core.concurrency.future;

import lombok.extern.log4j.Log4j2;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.stream.IntStream;

@Log4j2
public class Benchmarking {

    @Benchmark
    public void measureMultiplyOneThread() {
        multiplyOneThread();
    }

    public void multiplyOneThread() {
        IntStream.range(0, 1_000_000).map((i) -> i * i).sum();
    }

    @Benchmark
    public void measureMultiplyConcurrently() {
        multiplyConcurrently();
    }

    public void multiplyConcurrently() {
        IntStream.range(0, 1_000_000).parallel().map((i) -> i * i).sum();
    }
}
