package mergeIntervals;

import dev.posadskiy.java.ArrayUtils;

public class FindIntersections {

    /**
     * Find intersections for ranges.
     * Range is defined by [start, end] values
     *
     * @param intervals ranges for analysis
     * @return array with two elements - [start, end] of intersection
     */
    public int[] findIntersection(int[][] intervals) {
        var start = intervals[0][0];
        var end = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < start && intervals[i][1] > end) {
                continue;
            }

            start = Math.max(start, intervals[i][0]);
            end = Math.min(end, intervals[i][1]);

        }

        return new int[]{start, end};
    }

    /**
     * Find intersections for arrays.
     * Array could include unlimited number of elements
     *
     * @param intervals arrays for analysis
     * @return array with two elements - [start, end] of intersection
     */
    public int[] findIntersectionForAnyArray(int[][] intervals) {
        var start = ArrayUtils.min(intervals[0]);
        var end = ArrayUtils.max(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            final int currentStart = ArrayUtils.min(intervals[i]);
            final int currentEnd = ArrayUtils.max(intervals[i]);
            if (currentStart > end || currentEnd < start) {
                return new int[]{};
            }

            start = Math.max(start, currentStart);
            end = Math.min(end, currentEnd);
        }

        return new int[]{start, end};
    }


}
