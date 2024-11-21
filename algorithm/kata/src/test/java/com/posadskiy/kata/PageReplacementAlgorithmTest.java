package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class PageReplacementAlgorithmTest {
    
    @Test
    public void runner() {
        sampleTests(3, new int[] {1, 2, 3, 4, 3, 2, 5}, new int[] {5, 2, 3});
        sampleTests(5, new int[0], new int[] {-1, -1, -1, -1, -1});
        sampleTests(4, new int[] {5, 4, 3, 2, 3, 5, 2, 6, 7, 8}, new int[] {8, 6, 7, 2});
        sampleTests(4, new int[] {1, 1, 1, 2, 2, 3}, new int[] {1, 2, 3, -1});
        sampleTests(1, new int[] {5, 4, 3, 3, 4, 10}, new int[] {10});
        sampleTests(3, new int[] {1, 1, 1, 1, 1, 1, 1, 1}, new int[] {1, -1, -1});
        sampleTests(5, new int[] {10, 9, 8, 7, 7, 8, 7, 6, 5, 4, 3, 4, 3, 4, 5, 6, 5}, new int[] {5, 4, 3, 7, 6});
        sampleTests(6, new int[] {21, 24, 25, 8, 15, 21, 16, 13, 4, 25, 5, 11, 19, 12, 8, 7, 10, 24, 1, 14, 10, 18, 7, 19, 22, 16, 11, 25, 7, 7, 11, 11, 5, 2, 6, 25, 16, 8, 25, 13, 10, 4, 7, 16, 21, 3, 22, 12, 13, 19}, new int[] {3, 12, 21, 13, 19, 22});
    }

    public void sampleTests(int n, int[] referencesList, int[] expected) {
        int[] actual = PageReplacementAlgorithm.lru(n, referencesList);
        assertArrayEquals(expected, actual);
    }
}
