package dev.posadskiy.kata;

import org.junit.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

public class BubbleSortTest {

    @Test
    public void sort_AwesomeArray_SortedArrayReturned() {
        int[] arr = new int[]{6,4,3,2,7,2,4,7,4,5,6};

        int[] result = BubbleSort.sort(arr);

        System.out.println(Arrays.toString(arr));

        assertEquals(result[0], 2);
        assertEquals(result[1], 2);
    }

}