package com.posadskiy.java.release.v21.sequenced;

import java.util.ArrayList;
import java.util.List;

public class SequencedList {

    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        log.log(System.Logger.Level.INFO, values.getFirst());
        log.log(System.Logger.Level.INFO, values.getLast());

        values.addFirst(0);
        values.addLast(9);
        log.log(System.Logger.Level.INFO, values.getFirst());
        log.log(System.Logger.Level.INFO, values.getLast());

        values.removeFirst();
        values.removeLast();
        log.log(System.Logger.Level.INFO, values.getFirst());
        log.log(System.Logger.Level.INFO, values.getLast());

        var reversedValues = values.reversed();
        log.log(System.Logger.Level.INFO, reversedValues.getFirst());
        log.log(System.Logger.Level.INFO, reversedValues.getLast());
    }
}
