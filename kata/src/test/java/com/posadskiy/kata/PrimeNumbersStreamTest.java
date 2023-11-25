package com.posadskiy.kata;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class PrimeNumbersStreamTest {

    @Test
    public void test_0_10() {
        test(0, 10, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
    }

    @Test
    public void test_10_10() {
        test(10, 10, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71);
    }

    @Test
    public void test_100_10() {
        test(100, 10, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601);
    }

    @Test
    public void test_1000_10() {
        test(1000, 10, 7927, 7933, 7937, 7949, 7951, 7963, 7993, 8009, 8011, 8017);
    }

    @Test
    public void test_10000_10() {
        test(10000, 1, 104743);
    }

    @Test
    public void test_4_000_000_10() {
        test(4_000_000, 1, 67867979);
    }

    @Test
    public void test_40_000_000_10() {
        test(40_000_000, 1, 776531419);
    }

    private void test(int skip, int limit, int... expect) {
        int[] found = PrimeNumbersStream.stream().skip(skip).limit(limit).toArray();
        assertArrayEquals(expect, found);
    }
}
