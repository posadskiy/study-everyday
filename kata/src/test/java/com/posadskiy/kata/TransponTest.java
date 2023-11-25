package com.posadskiy.kata;

import org.junit.Test;

import java.util.Arrays;

public class TransponTest {

    @Test
    public void transpon_CorrectArray_TransponatedArrayReturned() {
        int[][] arr = new int[][]{{2, 4, 7}, {6, 8, 5}, {1, 3, 9}};
        System.out.println(Arrays.toString(arr[0]));
        System.out.println(Arrays.toString(arr[1]));
        System.out.println(Arrays.toString(arr[2]));
        System.out.println();


        Transpon.transpon(arr);

        System.out.println(Arrays.toString(arr[0]));
        System.out.println(Arrays.toString(arr[1]));
        System.out.println(Arrays.toString(arr[2]));
    }

}
