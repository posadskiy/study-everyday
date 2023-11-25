package com.posadskiy.algorithm.cycleSorting;

import com.posadskiy.utils.java.ArrayUtils;

public class CycleSorter {

    public int[] cycleSort(int[] arr) {
        var n = arr.length;
        var i = 0;
        while (i < n) {
            int correctPosition = arr[i];
            if (arr[i] < n && arr[i] != arr[correctPosition]) {
                ArrayUtils.swap(arr, i, correctPosition);
            } else {
                i++;
            }
        }

        return arr;
    }
}
