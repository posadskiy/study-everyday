package dev.posadskiy.kata;

public class RailFenceCipherBest {
    public static String encode(String s, int n) {
        return process(s, n, true);
    }

    public static String decode(String s, int n) {
        return process(s, n, false);
    }

    private static String process(String s, int n, boolean enc) {
        int len = s.length();
        int d = n * 2 - 2;
        StringBuilder sb = new StringBuilder(s);
        int counter = 0;
        for (int i = 0; i < n; i++) {
            int next = i == n - 1 ? d : d - i * 2;
            int index = i;

            while (index < len) {
                sb.setCharAt((enc ? counter++ : index), s.charAt(enc ? index : counter++));
                index += next;
                next = (next == d ? d : d - next);
            }
        }

        return sb.toString();
    }

}
