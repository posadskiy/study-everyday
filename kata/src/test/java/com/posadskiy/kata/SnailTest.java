package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Log4j2
public class SnailTest {
    @Test
    public void SnailTest1() {
        int[][] array = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int[] r = {1, 2, 3, 6, 9, 8, 7, 4, 5};
        test(array, r);
    }
    
    @Test
    public void testEmpty() {
        int [][] array = {{}};
        int[] r = {};
        
        test(array, r);
    }

    public String int2dToString(int[][] a) {
        return Arrays.stream(a).map(Arrays::toString).collect(joining("\n"));
    }

    public void test(int[][] array, int[] result) {
        String text = int2dToString(array) + " should be sorted to " + Arrays.toString(result);
        log.info(text);
        Assert.assertArrayEquals(result, Snail.snail(array));
    }
}
