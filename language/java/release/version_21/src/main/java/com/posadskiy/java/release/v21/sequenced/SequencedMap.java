package com.posadskiy.java.release.v21.sequenced;

import java.util.LinkedHashMap;

public class SequencedMap {
    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var values = new LinkedHashMap<>();
        values.put("1", 1);
        values.put("2", 2);
        values.put("3", 3);
        log.log(System.Logger.Level.INFO, values.firstEntry().getValue());
        log.log(System.Logger.Level.INFO, values.lastEntry().getValue());

        values.putFirst("0", 0);
        values.putLast("4", 4);
        log.log(System.Logger.Level.INFO, values.firstEntry().getValue());
        log.log(System.Logger.Level.INFO, values.lastEntry().getValue());

        values.pollFirstEntry();
        values.pollLastEntry();
        log.log(System.Logger.Level.INFO, values.firstEntry().getValue());
        log.log(System.Logger.Level.INFO, values.lastEntry().getValue());

        var valuesReversed = values.reversed();
        log.log(System.Logger.Level.INFO, valuesReversed.firstEntry().getValue());
        log.log(System.Logger.Level.INFO, valuesReversed.lastEntry().getValue());
    }
}
