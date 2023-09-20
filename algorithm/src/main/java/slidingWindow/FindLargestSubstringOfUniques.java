package slidingWindow;

import java.util.HashMap;
import java.util.Map;

public class FindLargestSubstringOfUniques {

    private final static Map<Character, Integer> values = new HashMap<>();

    /**
     * Find the largest substring of size k of unique elements
     * 
     * @param s - incoming string
     * @param k - sliding window size
     * @return - largest substring of uniques of the size 'k'
     */
    public int find(String s, int k) {
        var largest = 0;
        for (int i = 0; i < k; i++) {
            values.put(s.charAt(i), i);
        }
        largest = values.size();

        for (int i = k; i < s.length(); ++i) {
            final char charAt = s.charAt(i);
            values.put(charAt, i);

            final int diff = i - k;
            final char forRemoving = s.charAt(diff);
            if (values.containsKey(forRemoving) && values.get(forRemoving) == diff) {
                values.remove(forRemoving);
            }

            largest = Math.max(largest, values.size());
        }

        return largest;
    }
}
