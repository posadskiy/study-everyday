package com.posadskiy.algorithm.mergeIntervals;

import com.posadskiy.algorithm.mergeIntervals.FindIntersections;
import org.junit.Test;

import static org.junit.Assert.*;

public class FindIntersectionsTest {
    @Test
    public void test() {
        int[][] intervals = {{ 1, 6 },
            { 2, 8 },
            { 3, 10 },
            { 5, 8 }};

        final int[] result = new FindIntersections().findIntersection(intervals);
        
        assertArrayEquals(new int[]{5, 6}, result);
    }
    
    @Test
    public void testTwo() {
        int[][] intervals = {{3,1,2,4,5},{1,2,3,4},{3,4,5,6}};

        final int[] result = new FindIntersections().findIntersectionForAnyArray(intervals);

        assertArrayEquals(new int[]{3, 4}, result);
    }
    
    @Test
    public void testThree() {
        int[][] intervals = {{1,2,3},{4,5,6}};

        final int[] result = new FindIntersections().findIntersectionForAnyArray(intervals);

        assertArrayEquals(new int[]{}, result);
    }
}
