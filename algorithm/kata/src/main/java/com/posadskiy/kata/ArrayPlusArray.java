package com.posadskiy.kata;

import java.util.stream.IntStream;

public class ArrayPlusArray {
    public static int arrayPlusArray(int[] arr1, int[] arr2) {
        var result = 0;
        for (int i = 0; i < arr1.length + arr2.length; ++i) {
            result += i < arr1.length ? arr1[i] : arr2[i - arr1.length];
        }
        return result;
    }

    public static int arrayPlusArrayBetter(int[] arr1, int[] arr2) {
        return IntStream.concat(IntStream.of(arr1), IntStream.of(arr2))
            .sum();
    }
}
