package core.collection.map.hashtable;

import core.collection.map.hashmap.BadHashExample;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

/**
 * Table has Array of Entry inside
 * That's means it required re-balancing when new element adds and no space available in the array.
 * New array get size oldSize * 2 (oldSize << 1) * balancing_coefficient and all elements copies into new array.
 */
public class HashTableTesting {
    private final static Logger log = LogManager.getLogger(HashTableTesting.class);

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

