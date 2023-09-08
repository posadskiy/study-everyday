package com.posadskiy.language.java.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonMap {

    private final static Logger log = LogManager.getLogger(CommonMap.class);

    public static void main(String[] args) {
        flatMapTest();
        computeIfAbsentTest();
    }

    private static void computeIfAbsentTest() {
        final List<String> words = List.of("ability", "able", "about", "above", "accept", "according", "account", "across", "act", "action",
            "activity", "actually", "add");

        final HashMap<Integer, List<String>> map = new HashMap<>();
        words.forEach(value ->
            map.computeIfAbsent(value.length(), ArrayList::new)
                .add(value)
        );

        log.info(map.toString());
    }

    private static void flatMapTest() {
        final List<String> words = List.of("ability", "able", "about", "above", "accept", "according", "account", "across", "act", "action",
            "activity", "actually", "add");

        final HashMap<Integer, List<String>> map = new HashMap<>();
        
        words.forEach(value ->
            map.computeIfAbsent(value.length(), ArrayList::new)
                .add(value)
        );
        
        var flatMap = map.values().stream().flatMap(Collection::stream).toList();

        log.info(flatMap.toString());
    }

}
