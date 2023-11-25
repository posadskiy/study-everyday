package com.posadskiy.kata;

import java.util.Arrays;

public class SumOfDigits {
    public static int digital_root(int n) {
        int value = n;
        while (value > 9) {
            value = Integer.parseInt(
                Arrays.stream(String.valueOf(value).split(""))
                    .reduce("0", (v1, v2) -> String.valueOf(Integer.parseInt(v1) + Integer.parseInt(v2))));
        }
        
        return value;
    }
}
