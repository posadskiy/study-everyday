package com.posadskiy.kata;

import java.util.List;
import java.util.regex.Pattern;

public class FindSmileFace {
    public static int countSmileys(List<String> arr) {
        if (arr.isEmpty()) return 0;
        return Math.toIntExact(arr.stream().filter(value -> value.matches("^[:;][-~]?[)D]$")).count());
    }
}
