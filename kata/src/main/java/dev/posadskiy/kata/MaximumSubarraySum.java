package dev.posadskiy.kata;

public class MaximumSubarraySum {

    public static int sequence(int[] arr) {
        var sum = 0;
        for (int i = 0; i < arr.length; i++) {
            var currentSum = 0;
            for (int j = i; j < arr.length; j++) {
                currentSum += arr[j];
                if (currentSum > sum) {
                    sum = currentSum;
                }
            }
        }

        return sum;
    }

    public static int sequenceBest(int[] arr) {
        var maxEndingHere = 0;
        var maxSoFar = 0;
        var maximal = Integer.MIN_VALUE;
        for (int v : arr) {
            if (v > maximal) {
                maximal = v;
            }
            maxEndingHere = Math.max(0, maxEndingHere + v);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        return maxSoFar == 0 ? maximal : maxSoFar;
    }
}
