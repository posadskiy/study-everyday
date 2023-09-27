package core.collection.map.hashmap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Table has Array of Entry inside
 * That's means it required re-balancing when new element adds and no space available in the array.
 * New array get size oldSize * 2 (oldSize << 1) * balancing_coefficient and all elements copies into new array.
 * Default size is 16, but coefficient is 0.75, then with size in 16 elements map will be rebalanced after adding
 * 13th element.
 */
public class HashMapTesting {
    private final static Logger log = LogManager.getLogger(HashMapTesting.class);

    public static void main(String[] args) {
        searchComplexity();
    }

    private static void searchComplexity() {
        final Map<BadHashExample, String> table = new HashMap<>();

        var keys = IntStream.range(0, 20)
            .boxed()
            .collect(Collectors.toMap(value -> new BadHashExample(), String::valueOf));


        // All values are written into the same backet, last one has link on previous one.
        // Summary, it looks like 5 -> 3 -> 1
        // "Third" Entry has link to "Second" entry via .next field, and then "Second" on "First".
        // If more than TREEIFY_THRESHOLD - 1 (== 7 java17) values in the linked chain, this chain
        // will be converted into TreeMap for better performance
        log.info(keys);
    }
}
