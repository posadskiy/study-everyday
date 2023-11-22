package com.posadskiy.java.release.v21.sequenced;

import java.util.LinkedHashSet;

public class SequencedSet {
    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var values = new LinkedHashSet<>();
        values.add(1);
        values.add(2);
        values.add(3);
        values.add(4);
        values.add(5);
        log.log(System.Logger.Level.INFO, values.getFirst());
        log.log(System.Logger.Level.INFO, values.getLast());

        values.addFirst(0);
        values.addLast(6);
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
