package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In this kata, your task is to create all permutations of a non-empty input string and remove duplicates, if present.
 * <p>
 * Create as many "shufflings" as you can!
 * <p>
 * Examples:
 * <p>
 * With input 'a':
 * Your function should return: ['a']
 * <p>
 * With input 'ab':
 * Your function should return ['ab', 'ba']
 * <p>
 * With input 'abc':
 * Your function should return ['abc','acb','bac','bca','cab','cba']
 * <p>
 * With input 'aabb':
 * Your function should return ['aabb', 'abab', 'abba', 'baab', 'baba', 'bbaa']
 * Note: The order of the permutations doesn't matter.
 * <p>
 * Good luck!
 */
public class SoManyPermutations {
    public static List<String> singlePermutations(String s) {
        if (s.length() == 1) {
            return Collections.singletonList(s);
        }

        List<String> result = new ArrayList<>(Collections.singleton(""));
        final String[] split = s.split("");
        for (String value : split) {
            List<String> newResult = new ArrayList<>();
            for (int j = 0; j < result.size(); j++) {
                final String current = result.get(j);
                for (int k = 0; k < current.split("").length; k++) {
                    if (result.size() < 2) {
                        newResult.add(value);
                        newResult.add(value);
                        continue;
                    }

                    newResult.add(current.substring(0, k) + value + current.substring(k));
                    newResult.add(current.substring(0, k + 1) + value + current.substring(k + 1));
                }
            }

            result = newResult;
        }

        return result.stream().distinct().collect(Collectors.toList());
    }
}
