package slidingWindow;

public class FindLargestSubstringOfEquals {

    /**
     * Find the largest substring of size k of equals elements
     * 
     * @param s - incoming string
     * @param k - sliding window size
     * @return - largest substring of equals of the size 'k'
     */
    public int find(String s, int k) {
        var largest = 0;
        char lastElement = Character.MAX_VALUE;
        var current = 0;
        for (int i = 0; i < k; i++) {
            if (s.charAt(i) == lastElement) {
                current++;
            } else {
                largest = Math.max(largest, current);
                current = 1;
            }
            lastElement = s.charAt(i);
        }
        largest = Math.max(largest, current);

        for (int i = k; i < s.length(); ++i) {
            if (s.charAt(i) == lastElement) {
                current++;
            } else {
                current = 1;
                largest = Math.max(largest, current);
            }
            lastElement = s.charAt(i);

            largest = Math.max(largest, current);
        }

        return largest;
    }
}
