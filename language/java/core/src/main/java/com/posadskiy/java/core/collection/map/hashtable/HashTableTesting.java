package com.posadskiy.java.core.collection.map.hashtable;

import com.posadskiy.java.core.collection.map.hashmap.BadHashExample;
import lombok.extern.log4j.Log4j2;

import java.util.Hashtable;

/**
 * Table has Array of Entry inside
 * That's means it required re-balancing when new element adds and no space available in the array.
 * New array get size oldSize * 2 (oldSize << 1) * balancing_coefficient and all elements copies into new array.
 */
@Log4j2
public class HashTableTesting {

    public static void main(String[] args) {
        searchComplexity();
    }

    private static void searchComplexity() {
        final Hashtable<BadHashExample, String> table = new Hashtable<>();

        final BadHashExample first = new BadHashExample();
        final BadHashExample second = new BadHashExample();
        final BadHashExample third = new BadHashExample();

        table.put(first, "1");
        table.put(second, "3");
        table.put(third, "5");

        // All values are written into the same basket, last one has link on previous one.
        // Summary, it looks like 5 -> 3 -> 1
        // "Third" Entry has link to "Second" entry via .next field, and then "Second" on "First".
        log.info(table);
    }
}

