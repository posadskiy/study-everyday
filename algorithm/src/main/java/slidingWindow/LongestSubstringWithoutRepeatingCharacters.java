package slidingWindow;

import java.util.HashMap;

public class LongestSubstringWithoutRepeatingCharacters {

    /**
     * Find the longest substring without repeating characters
     * 
     * @param s - incoming string
     * @return longest substring of non-repeated elements
     */
    public int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = 0, j = 0; i < s.length(); ++i) {
            if (map.containsKey(s.charAt(i))) {
                j = Math.max(j, map.get(s.charAt(i)) + 1);
            }
            map.put(s.charAt(i), i);
            max = Math.max(max, i - j + 1);
        }
        return max;
    }
}
