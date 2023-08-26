package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.List;

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
