package com.posadskiy.java.release.v21.sequenced;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedHashSet;

@Log4j2
public class SequencedSet {

    public static void main(String[] args) {
        var values = new LinkedHashSet<>();
        values.add(1);
        values.add(2);
        values.add(3);
        values.add(4);
        values.add(5);
        log.info(values.getFirst());
        log.info(values.getLast());

        values.addFirst(0);
        values.addLast(6);
        log.info(values.getFirst());
        log.info(values.getLast());

        values.removeFirst();
        values.removeLast();
        log.info(values.getFirst());
        log.info(values.getLast());

        var reversedValues = values.reversed();
        log.info(reversedValues.getFirst());
        log.info(reversedValues.getLast());
    }
}
