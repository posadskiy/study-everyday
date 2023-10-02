package dev.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class TwoSumTest {
    
    @Test
    public void test() {
        final int[] calculated = new TwoSum().twoSumSorted(new int[]{2, 7, 11, 15}, 9);
        final int[] expected = {0, 1};
        assertArrayEquals(calculated, expected);
    }
    
    @Test
    public void testUnsorted() {
        final int[] calculated = new TwoSum().twoSumUnsorted(new int[]{2, 7, 11, 15}, 9);
        final int[] expected = {0, 1};
        assertArrayEquals(calculated, expected);
    }
    
    @Test
    public void testHashMap() {
        final int[] calculated = new TwoSum().hashMapSolution(new int[]{3, 3}, 6);
        final int[] expected = {0, 1};
        assertArrayEquals(calculated, expected);
    }

}
