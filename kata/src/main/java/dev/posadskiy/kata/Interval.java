package dev.posadskiy.kata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

class Interval {
    static int sumIntervals(int[][] intervals) {
        if (intervals == null) return 0;
        Map<Integer, Boolean> res = new HashMap<>();

        for (int[] inter : intervals) {
            IntStream.range(inter[0], inter[1]).forEach(item -> res.put(item, true));
        }
        return res.keySet().size();
    }

    public static int sumIntervalsBetter(int[][] intervals) {
        return intervals == null ? 0 : (int) Arrays.stream(intervals)
                .flatMapToInt(interval -> IntStream.range(interval[0], interval[1]))
                .distinct()
                .count();
    }
}
