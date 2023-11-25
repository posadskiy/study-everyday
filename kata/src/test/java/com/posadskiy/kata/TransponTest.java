package com.posadskiy.kata;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;

@Log4j2
public class TransponTest {

    @Test
    public void transpon_CorrectArray_TransponatedArrayReturned() {
        int[][] arr = new int[][]{{2, 4, 7}, {6, 8, 5}, {1, 3, 9}};
        log.info(Arrays.toString(arr[0]));
        log.info(Arrays.toString(arr[1]));
        log.info(Arrays.toString(arr[2]));

        Transpon.transpon(arr);

        log.info(Arrays.toString(arr[0]));
        log.info(Arrays.toString(arr[1]));
        log.info(Arrays.toString(arr[2]));
    }

}
