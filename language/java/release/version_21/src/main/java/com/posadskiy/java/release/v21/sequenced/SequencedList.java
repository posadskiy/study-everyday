package com.posadskiy.java.release.v21.sequenced;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SequencedList {

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        log.info(values.getFirst());
        log.info(values.getLast());

        values.addFirst(0);
        values.addLast(9);
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
