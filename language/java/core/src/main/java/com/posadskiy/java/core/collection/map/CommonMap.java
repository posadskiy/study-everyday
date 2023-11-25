package com.posadskiy.java.core.collection.map;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Log4j2
public class CommonMap {

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
