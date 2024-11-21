package com.posadskiy.algorithm.binarySearch;

import java.util.List;
import java.util.Optional;

public class BinarySearcher<T> {
    public static <T extends Comparable<T>> Optional<Integer> search(List<T> values, T value) {
        int start = 0;
        int end = values.size();
        int middle = getMiddle(start, end);

        while (start != end) {
            final T obj = values.get(middle);
            if (value.equals(obj)) {
                return Optional.of(middle);
            }

            if (obj.compareTo(value) < 0) {
                end = middle;
            } else {
                start = middle;
            }

            middle = getMiddle(start, end);
        }

        return Optional.empty();
    }

    private static int getMiddle(int start, int end) {
        return (int) Math.floor((start + (end - start)) / 2.0);
    }
}
