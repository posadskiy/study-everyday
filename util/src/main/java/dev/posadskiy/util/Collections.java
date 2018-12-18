package dev.posadskiy.util;

import java.util.List;

public class Collections {

    public static <T extends Comparable<? super T>> void sort(List<T> items) {
        java.util.Collections.sort(items);
    }

}
