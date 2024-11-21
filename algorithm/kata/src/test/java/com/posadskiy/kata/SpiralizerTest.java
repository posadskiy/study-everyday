package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpiralizerTest {
    @Test
    public void test5() {
        assertArrayEquals(
            new int[][] {
                { 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 1 },
                { 1, 1, 1, 0, 1 },
                { 1, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1 }
            },
            Spiralizer.spiralize(5));
    }
    
    @Test
    public void test6() {
        assertArrayEquals(
            new int[][] {
                { 1, 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 0, 1 },
                { 1, 0, 0, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1 }
            },
            Spiralizer.spiralize(6));
    }

    @Test
    public void test8() {
        assertArrayEquals(
            new int[][] {
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 0, 1, 0, 1 },
                { 1, 0, 1, 1, 1, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
            },
            Spiralizer.spiralize(8));
    }

}
