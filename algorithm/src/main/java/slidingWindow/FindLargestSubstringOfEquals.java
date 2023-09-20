package slidingWindow;

public class FindLargestSubstringOfEquals {

    public int find(String s, int k) {
        var largest = 0;
        Character lastElement = Character.MAX_VALUE;
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
