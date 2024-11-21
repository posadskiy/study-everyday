package com.posadskiy.kata;

import java.util.ArrayList;
import java.util.List;

/**
 * A format for expressing an ordered list of integers is to use a comma separated list of either
 * <p>
 * individual integers or a range of integers denoted by the starting integer separated from the end integer in the range by a dash, '-'.
 * The range includes all integers in the interval including both endpoints. It is not considered a range unless it spans at least 3
 * numbers. For example "12,13,15-17" Complete the solution so that it takes a list of integers in increasing order and returns a correctly
 * formatted string in the range format.
 * <p>
 * Example:
 * <p>
 * Solution.rangeExtraction(new int[] {-10, -9, -8, -6, -3, -2, -1, 0, 1, 3, 4, 5, 7, 8, 9, 10, 11, 14, 15, 17, 18, 19, 20}) # returns
 * "-10--8,-6,-3-1,3-5,7-11,14,15,17-20"
 */
public class RangeExtraction {

    public static String rangeExtraction(int[] arr) {
        StringBuilder result = new StringBuilder();
        final List<Range> ranges = findRanges(arr);
        for (Range range : ranges) {
            if (range.from == range.to) {
                result.append(range.from).append(",");
                continue;
            }
            if (range.to - range.from == 1) {
                result.append(range.from).append(",").append(range.to).append(",");
                continue;
            }

            result.append(range.from).append("-").append(range.to).append(",");
        }

        if (result.charAt(result.length() - 1) == ',') {
            result.delete(result.length() - 1, result.length());
        }

        return result.toString();
    }

    private static List<Range> findRanges(int[] arr) {
        List<Range> ranges = new ArrayList<>();
        Range range = null;
        int last = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (range == null) {
                range = new Range();
                range.from = arr[i];
                continue;
            }

            if (last + 1 != arr[i]) {
                range.to = last;
                ranges.add(range);
                range = new Range();
                range.from = arr[i];
            }

            last = arr[i];

            if (i == arr.length - 1) {
                range.to = arr[i];
                ranges.add(range);
            }
        }

        return ranges;
    }

}

class Range {

    public int from;
    public int to;
}
