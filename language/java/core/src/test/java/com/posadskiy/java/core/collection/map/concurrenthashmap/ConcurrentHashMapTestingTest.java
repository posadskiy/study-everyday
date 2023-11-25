package com.posadskiy.java.core.collection.map.concurrenthashmap;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTestingTest {

    @Test(expected = ConcurrentModificationException.class)
    public void whenRemoveAndAddOnHashMap_thenConcurrentModificationError() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Value");
        map.put(2, "Another Value");
        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(map);
        Iterator<Map.Entry<Integer, String>> iterator = synchronizedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            synchronizedMap.put(3, "Modification");
            iterator.next();
        }
    }

    @Test
    public void noException() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Value");
        map.put(2, "Another Value");
        Map<Integer, String> synchronizedMap = new ConcurrentHashMap<>();
        Iterator<Map.Entry<Integer, String>> iterator = synchronizedMap.entrySet().iterator();
        while (iterator.hasNext()) {
            synchronizedMap.put(3, "Modification");
            iterator.next();
        }
    }
}
