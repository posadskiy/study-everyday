package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create a function that takes a positive integer and returns the next bigger number that can be formed by rearranging its digits. For
 * example:
 * <p>
 * 12 ==> 21 513 ==> 531 2017 ==> 2071 If the digits can't be rearranged to form a bigger number, return -1 (or nil in Swift, None in
 * Rust):
 * <p>
 * 9 ==> -1 111 ==> -1 531 ==> -1
 */
public class NextNumberWithSameDigits {

    public static long nextBiggerNumber(long n) {
        final String input = String.valueOf(n);
        for (int i = input.length() - 2; i >= 0; --i) {
            final List<String> permutations = getPermutations(input.substring(i, i + 2));
            permutations.sort(Comparator.naturalOrder());
            final String basis = input.substring(0, i);
            final String suffix = input.substring(i + 2);
            for (String permutation : permutations) {
                long suggestedValue = Long.parseLong(basis + permutation + suffix);
                if (suggestedValue > n) {
                    final List<String> permutations1 = getPermutations(permutation + suffix);
                    permutations1.sort(Comparator.naturalOrder());
                    for (String s : permutations1) {
                        suggestedValue = Long.parseLong(basis + s);
                        if (suggestedValue > n) {
                            return suggestedValue;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static List<String> getPermutations(String numberValue) {
        final String[] split = numberValue.split("");
        List<String> result = new ArrayList<>(Collections.singleton(""));
        for (int i = 0; i < split.length; i++) {
            String value = split[i];
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

            if (i != 0) {
                newResult = newResult.stream().distinct().collect(Collectors.toList());
                if (newResult.size() == 1) {
                    newResult.add(newResult.get(0));
                }
            }
            result = newResult;
        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    public static long nextBiggerNumberBest(long n) {
        char[] s = String.valueOf(n).toCharArray();
        for (int i = s.length - 2; i >= 0; i--) {
            for (int j = s.length - 1; j > i; j--) {
                if (s[i] < s[j]) {
                    char tmp = s[i];
                    s[i] = s[j];
                    s[j] = tmp;
                    Arrays.sort(s, i + 1, s.length);
                    return Long.parseLong(String.valueOf(s));
                }
            }
        }
        return -1;
    }

}
