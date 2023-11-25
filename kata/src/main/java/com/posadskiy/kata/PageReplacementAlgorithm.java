package com.posadskiy.kata;

import java.util.*;

public class PageReplacementAlgorithm {
    public static int[] lru(int n, int[] referencesList) {
        List<Integer> elements = new ArrayList<>();

        for (int i = 0; i < referencesList.length; ++i) {
            if (elements.contains(referencesList[i])) {
                continue;
            }

            if (elements.size() < n) {
                elements.add(referencesList[i]);
                continue;
            }

            List<Integer> lastUsage = new ArrayList<>();
            int insertIndex = i - 1;
            while (lastUsage.size() < n-1 && insertIndex > 0) {
                if (!lastUsage.contains(referencesList[insertIndex])) {
                    lastUsage.add(referencesList[insertIndex]);
                }
                insertIndex--;
            }
            final Integer first = elements.stream().filter(value -> lastUsage.stream().noneMatch(value::equals)).findFirst().get();
            final int removedIndex = elements.indexOf(first);
            elements.remove(first);
            elements.add(removedIndex, referencesList[i]);
        }

        if (elements.size() < n) {
            for (int i = elements.size(); i < n; ++i) {
                elements.add(i, -1);
            }
        }
        
        return elements.stream().mapToInt(i -> i).toArray();
    }
}
