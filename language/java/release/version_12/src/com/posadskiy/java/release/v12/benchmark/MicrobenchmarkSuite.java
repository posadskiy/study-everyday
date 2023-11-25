package com.posadskiy.java.release.v12.benchmark;

import com.posadskiy.kata.PrimeNumbersStream;
import java.io.IOException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

/**
 * JEP 230: Microbenchmark Suite
 * <p>
 * <a href="https://openjdk.org/jeps/230">Docs</a>
 */
public class MicrobenchmarkSuite {

    public static void main(String[] args) {
        test();
        try {
            org.openjdk.jmh.Main.main(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.AverageTime)
    public static void test() {
        PrimeNumbersStream.stream();
    }

}
