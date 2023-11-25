package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@Log4j2
public class FindUniqueTest {
    public static final int LOOP_COUNT = 100_000;
    public static final int ARRAY_LENGTH = 100_000;
    public static final int ARRAY_VALUE = 112233;
    public static final int ARRAY_UNIQUE_VALUE = 1;


    @Test
    public void simpleArray1() {
        final int[] array = new int[ARRAY_LENGTH];
        Arrays.fill(array, ARRAY_VALUE);
        array[ARRAY_LENGTH/2] = ARRAY_UNIQUE_VALUE;
        
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < LOOP_COUNT; ++i) {
            assertEquals(ARRAY_UNIQUE_VALUE, FindUnique.bestOne(array));
        }
        watch.stop();
        log.info("Time Elapsed: " + watch.getTime());
    }

    @Test
    public void simpleArray2() {
        final int[] array = new int[ARRAY_LENGTH];
        Arrays.fill(array, ARRAY_VALUE);
        array[ARRAY_LENGTH/2] = ARRAY_UNIQUE_VALUE;

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < LOOP_COUNT; ++i) {
            assertEquals(ARRAY_UNIQUE_VALUE, FindUnique.strayAnotherWay(array));
        }
        watch.stop();
        log.info("Time Elapsed: " + watch.getTime());
    }

    @Test
    public void simpleArray3() {
        final int[] array = new int[ARRAY_LENGTH];
        Arrays.fill(array, ARRAY_VALUE);
        array[ARRAY_LENGTH/2] = ARRAY_UNIQUE_VALUE;

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < LOOP_COUNT; ++i) {
            assertEquals(ARRAY_UNIQUE_VALUE, FindUnique.stray(array));
        }
        watch.stop();
        log.info("Time Elapsed: " + watch.getTime());
    }

}
