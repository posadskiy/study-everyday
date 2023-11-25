package com.posadskiy.kata;

import java.util.stream.IntStream;

public class PrimeNumbersStream {

    private static final int SIZE = 784000000;
    private static final int[] prime = new int[SIZE / 64 + 1];

    public static IntStream stream() {
        return bitWiseSieve();
    }

    private static boolean isPrime(int x) {
        return (prime[x / 64] & (1 << ((x >> 1) & 31))) == 0;
    }

    static void makeComposite(int x) {
        prime[x / 64] |= (1 << ((x >> 1) & 31));
    }

    static IntStream bitWiseSieve() {
        for (int i = 3; i * i <= PrimeNumbersStream.SIZE; i += 2) {
            if (isPrime(i)) {
                for (int j = i * i; j < PrimeNumbersStream.SIZE; j += i << 1) {
                    makeComposite(j);
                }
            }
        }

        return IntStream.iterate(1, i -> i + 2).limit(PrimeNumbersStream.SIZE/2).filter(PrimeNumbersStream::isPrime);
    }

}
