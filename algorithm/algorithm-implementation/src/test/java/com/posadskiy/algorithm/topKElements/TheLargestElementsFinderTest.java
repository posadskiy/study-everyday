package com.posadskiy.algorithm.topKElements;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TheLargestElementsFinderTest {

    @Test
    public void test() {
        final List<Integer> values = ListGenerator.generate();
        var size = 3;
        var expected = List.of(44, 34, 22);

        final List<Integer> result = TheLargestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }

    @Test
    public void testOne() {
        final List<Integer> values = ListGenerator.generate();
        var size = 5;
        var expected = List.of(44, 34, 22, 7, 7);

        final List<Integer> result = TheLargestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }

    @Test
    public void testTwo() {
        final List<Integer> values = ListGenerator.generate();
        var size = 2;
        var expected = List.of(44, 34);

        final List<Integer> result = TheLargestElementsFinder.finder(values, size);

        assertEquals(size, result.size());
        for (Integer value : expected) {
            assertTrue(result.contains(value));
        }
    }

}
