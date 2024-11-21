package com.posadskiy.kata;

import java.util.stream.IntStream;

/**
 * Given an array, find the int that appears an odd number of times.
 *
 * There will always be only one integer that appears an odd number of times.
 */
class FindOdd {
    static int findIt(int[] a) {
        return IntStream.of(a).reduce(0, (x, y) -> x ^ y);
    }
}
