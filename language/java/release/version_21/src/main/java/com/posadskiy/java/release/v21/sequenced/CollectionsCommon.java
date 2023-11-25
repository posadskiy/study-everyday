package com.posadskiy.java.release.v21.sequenced;

import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Log4j2
public class CollectionsCommon {

    public static void main(String[] args) {
        var list = Collections.unmodifiableSequencedCollection(List.of(1, 2, 3));
        try {
            list.add(4);
        } catch (UnsupportedOperationException e) {
            // e.getMessage() is null for some reason
            //log.log(System.Logger.Level.INFO, e.getMessage());
            log.info("Can't add value to unmodifiable collection");
        }

        var map = Collections.unmodifiableSequencedMap(new LinkedHashMap<>());
        try {
            map.putFirst("0", 0);
        } catch (Exception e) {
            log.info("Can't add value to unmodifiable collection");
        }
    }
}
