package com.posadskiy.kata;

import java.util.Arrays;

public class CountPositivesSumNegatives {

    public static int[] countPositivesSumNegatives(int[] input) {
        if (input == null || input.length == 0) {
            return new int[]{};
        }

        var positiveCount = 0;
        var negativeSum = 0;

        for (int j : input) {
            if (j > 0) {
                positiveCount++;
            }
            if (j < 0) {
                negativeSum += j;
            }
        }

        return new int[]{positiveCount, negativeSum};
    }

    public static int[] countPositivesSumNegativesBetter(int[] input) {
        if (input == null || input.length == 0) {
            return new int[]{};
        }

        var result = new int[]{0, 0};
        Arrays.stream(input)
            .forEach(value -> {
                if (value > 0) {
                    result[0] += 1;
                } else {
                    result[1] += value;
                }
            });
        return result;
    }

}
