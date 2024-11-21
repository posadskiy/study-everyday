package com.posadskiy.algorithm.twoIterators;

import com.posadskiy.algorithm.twoIterators.SquaresOfSortedArray;
import org.junit.Test;

import static org.junit.Assert.*;

public class SquaresOfSortedArrayTest {

    @Test
    public void testOne() {
        SquaresOfSortedArray squaresOfSortedArray = new SquaresOfSortedArray();

        final int[] result = squaresOfSortedArray.sortedSquares(new int[]{-4, -1, 0, 3, 10});
        
        assertArrayEquals(new int[]{0,1,9,16,100}, result);
    }
}
