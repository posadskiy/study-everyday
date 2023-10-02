package dev.posadskiy.kata;

import java.util.Arrays;

public class StringAnagramChecker {
    public boolean isAnagram(String s, String t) {
        final char[] charArrayS = s.toCharArray();
        Arrays.sort(charArrayS);
        final char[] charArrayT = t.toCharArray();
        Arrays.sort(charArrayT);
        return Arrays.compare(charArrayS, charArrayT) == 0;
    }

    public boolean isAnagramBest(String s, String t) {
        int[] alphabet = new int[26];
        for (int i = 0; i < s.length(); i++) alphabet[s.charAt(i) - 'a']++;
        for (int i = 0; i < t.length(); i++) alphabet[t.charAt(i) - 'a']--;
        for (int i : alphabet) if (i != 0) return false;
        return true;
    }
}
