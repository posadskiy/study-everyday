package com.posadskiy.kata;

import java.util.List;

public class SortMyTextbooks {
    public static List<String> sort(List<String> textbooks) {
        return textbooks.stream().sorted(String::compareToIgnoreCase).toList();
    }
}
