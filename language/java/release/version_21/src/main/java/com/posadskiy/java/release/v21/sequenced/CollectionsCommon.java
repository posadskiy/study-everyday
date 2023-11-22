package com.posadskiy.java.release.v21.sequenced;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class CollectionsCommon {
    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        var list = Collections.unmodifiableSequencedCollection(List.of(1, 2, 3));
        try {
            list.add(4);
        } catch (UnsupportedOperationException e) {
            // e.getMessage() is null for some reason
            //log.log(System.Logger.Level.INFO, e.getMessage());
            log.log(System.Logger.Level.INFO, "Can't add value to unmodifiable collection");
        }

        var map = Collections.unmodifiableSequencedMap(new LinkedHashMap<>());
        try {
            map.putFirst("0", 0);
        } catch (Exception e) {
            log.log(System.Logger.Level.INFO, "Can't add value to unmodifiable collection");
        }
    }
}
