package com.posadskiy.java.release.v21.sequenced;

import lombok.extern.log4j.Log4j2;

import java.util.LinkedHashMap;

@Log4j2
public class SequencedMap {

    public static void main(String[] args) {
        var values = new LinkedHashMap<>();
        values.put("1", 1);
        values.put("2", 2);
        values.put("3", 3);
        log.info(values.firstEntry().getValue());
        log.info(values.lastEntry().getValue());

        values.putFirst("0", 0);
        values.putLast("4", 4);
        log.info(values.firstEntry().getValue());
        log.info(values.lastEntry().getValue());

        values.pollFirstEntry();
        values.pollLastEntry();
        log.info(values.firstEntry().getValue());
        log.info(values.lastEntry().getValue());

        var valuesReversed = values.reversed();
        log.info(valuesReversed.firstEntry().getValue());
        log.info(valuesReversed.lastEntry().getValue());
    }
}
