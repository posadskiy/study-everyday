package dev.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDifferenceTest {

    @Test
    public void sampleTests() {
        assertArrayEquals(new int[] {2}, ArrayDifference.arrayDiff(new int [] {1,2}, new int[] {1}));
        assertArrayEquals(new int[] {2,2}, ArrayDifference.arrayDiff(new int [] {1,2,2}, new int[] {1}));
        assertArrayEquals(new int[] {1}, ArrayDifference.arrayDiff(new int [] {1,2,2}, new int[] {2}));
        assertArrayEquals(new int[] {1,2,2}, ArrayDifference.arrayDiff(new int [] {1,2,2}, new int[] {}));
        assertArrayEquals(new int[] {}, ArrayDifference.arrayDiff(new int [] {}, new int[] {1,2}));
    }

}
