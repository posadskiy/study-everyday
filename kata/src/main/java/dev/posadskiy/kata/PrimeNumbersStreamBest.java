package dev.posadskiy.kata;

import java.util.BitSet;
import java.util.stream.IntStream;

public class PrimeNumbersStreamBest {

    public static final int SIZE = 810000000;
    public static final int SIZE_SQRT = 28460;

    private static BitSet bitset = new PrimeBitSet().bitset;

    public static IntStream stream() {
        return IntStream.iterate(2, i -> bitset.nextSetBit(i + 1));
    }
}

class PrimeBitSet {

    BitSet bitset;

    PrimeBitSet() {
        bitset = new BitSet(PrimeNumbersStreamBest.SIZE);
        bitset.set(2, PrimeNumbersStreamBest.SIZE - 1);

        int i = 1;
        while (i < PrimeNumbersStreamBest.SIZE_SQRT) {
            i = bitset.nextSetBit(i + 1);
            for (int j = i * i; j < PrimeNumbersStreamBest.SIZE; j += i) {
                bitset.clear(j);
            }
        }
    }
}
