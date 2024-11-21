package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

@Log4j2
public class BubbleSortTest {

    @Test
    public void sort_AwesomeArray_SortedArrayReturned() {
        int[] arr = new int[]{6,4,3,2,7,2,4,7,4,5,6};

        int[] result = BubbleSort.sort(arr);

        log.info(Arrays.toString(arr));

        assertEquals(result[0], 2);
        assertEquals(result[1], 2);
    }

}
