package core.collection.map.concurrenthashmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In ConcurrentHashMap, read operations are non-blocking, 
 * whereas write operations take a lock on a particular segment or bucket.
 */
public class ConcurrentHashMapTesting {

    public void concurrentHashMapVsCollectionsSyncronizedMap() {
        final ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        final Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<String, Integer>());

        // Syncronized is on inserting process only
        concurrentHashMap.put("Vienna", 2);

        // Syncronized is on whole method
        synchronizedMap.put("Vienna", 2);

        // No sync
        concurrentHashMap.get("Vienna");

        // Whole method sync
        synchronizedMap.get("Vienna");
    }
}
