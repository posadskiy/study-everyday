package dev.posadskiy.kata;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
        System.out.println("Time Elapsed: " + watch.getTime());
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
        System.out.println("Time Elapsed: " + watch.getTime());
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
        System.out.println("Time Elapsed: " + watch.getTime());
    }

}
