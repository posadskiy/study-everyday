package dev.posadskiy.kata;

import java.util.ArrayList;
import java.util.List;

/**
 * Given the string representations of two integers, return the string representation of the sum of those integers.
 * <p>
 * For example:
 * <p>
 * sumStrings('1','2') // => '3' A string representation of an integer will contain no characters besides the ten numerals "0" to "9".
 * <p>
 * I have removed the use of BigInteger and BigDecimal in java
 * <p>
 * Python: your solution need to work with huge numbers (about a million digits), converting to int will not work.
 */
public class SumStringsAsNumbers {

    public static String sumStrings(String a, String b) {
        if (a.equals("")) {
            return b;
        }
        if (b.equals("")) {
            return a;
        }
        String first = a;
        while (first.charAt(0) == '0') {
            first = first.replaceFirst("0", "");
        }
        String second = b;
        while (second.charAt(0) == '0') {
            second = second.replaceFirst("0", "");
        }
        final int max = Math.max(first.length(), second.length());
        List<String> result = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < max; ++i) {
            int sum = 0;
            if (temp == 1) {
                sum = 1;
                temp = 0;
            }
            if (first.length() - i > 0) {
                sum += Integer.parseInt(first.substring(first.length() - i - 1, first.length() - i));
            }
            if (second.length() - i > 0) {
                sum += Integer.parseInt(second.substring(second.length() - i - 1, second.length() - i));
            }
            if (sum > 9) {
                temp = 1;
                sum -= 10;
            }

            result.add(0, String.valueOf(sum));
        }

        if (temp == 1) {
            result.add(0, "1");
        }

        return String.join("", result);
    }

}
