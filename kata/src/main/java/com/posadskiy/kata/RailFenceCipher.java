package com.posadskiy.kata;

import java.util.Arrays;
import java.util.OptionalInt;

/**
 * Create two functions to encode and then decode a string using the Rail Fence Cipher. This cipher is used to encode a string by placing
 * each character successively in a diagonal along a set of "rails". First start off moving diagonally and down. When you reach the bottom,
 * reverse direction and move diagonally and up until you reach the top rail. Continue until you reach the end of the string. Each "rail" is
 * then read left to right to derive the encoded string.
 * <p>
 * For example, the string "WEAREDISCOVEREDFLEEATONCE" could be represented in a three rail system as follows:
 * <p>
 * W       E       C       R       L       T       E E   R   D   S   O   E   E   F   E   A   O   C A       I       V       D       E       N
 * The encoded string would be:
 * <p>
 * WECRLTEERDSOEEFEAOCAIVDEN Write a function/method that takes 2 arguments, a string and the number of rails, and returns the ENCODED
 * string.
 * <p>
 * Write a second function/method that takes 2 arguments, an encoded string and the number of rails, and returns the DECODED string.
 * <p>
 * For both encoding and decoding, assume number of rails >= 2 and that passing an empty string will return an empty string.
 * <p>
 * Note that the example above excludes the punctuation and spaces just for simplicity. There are, however, tests that include punctuation.
 * Don't filter out punctuation as they are a part of the string.
 */
public class RailFenceCipher {

    static String encode(String s, int n) {
        boolean[] status = new boolean[n];
        StringBuilder result = new StringBuilder();
        int upPosition = 0;

        while (upPosition < n) {
            int currentPosition = upPosition;
            if (currentPosition >= rangeMiddle(n)) {
                status[currentPosition] = true;
            }
            while (currentPosition < s.length()) {
                result.append(s.charAt(currentPosition));
                int step = calculateStep(upPosition, status);
                currentPosition += step;
            }
            upPosition++;
        }
        return result.toString();
    }

    static String decode(String s, int n) {
        if (s.length() == 0) {
            return "";
        }
        boolean[] status = new boolean[n];
        int[] symbolsNumberOnRows = new int[n];
        int upPosition = 0;

        while (upPosition < n) {
            int currentPosition = upPosition;
            if (currentPosition >= rangeMiddle(n)) {
                status[currentPosition] = true;
            }
            while (currentPosition < s.length()) {
                symbolsNumberOnRows[upPosition]++;
                int step = calculateStep(upPosition, status);
                currentPosition += step;
            }
            upPosition++;
        }

        String[] rows = new String[n];
        int counter = 0;
        for (int i = 0; i < rows.length; i++) {
            final int length = symbolsNumberOnRows[i];
            rows[i] = s.substring(counter, counter + length);
            counter += length;
        }

        final OptionalInt maxOptional = Arrays.stream(rows).mapToInt(String::length).max();
        if (!maxOptional.isPresent()) {
            return "";
        }

        int[] positions = new int[n];
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows.length; i++) {
            result.append(rows[i].charAt(0));
            positions[i]++;
        }

        int currentRow = n - 1;
        int nextRow = n - 2;
        while (result.length() != s.length()) {
            result.append(rows[nextRow].charAt(positions[nextRow]));
            positions[nextRow]++;

            if (nextRow > currentRow) {
                if (nextRow == n - 1) {
                    nextRow = currentRow;
                    currentRow = nextRow + 1;
                    continue;
                }

                nextRow++;
                currentRow++;
            } else {
                if (nextRow == 0) {
                    nextRow = currentRow;
                    currentRow = nextRow - 1;
                    continue;
                }

                nextRow--;
                currentRow--;
            }
        }

        return result.toString();
    }

    private static int rangeMiddle(int n) {
        return (int) Math.ceil(n / 2.0);
    }

    private static int calculateStep(int row, boolean[] status) {
        final int length = status.length;
        final int fullWidth = 2 * (length - 1);
        if (row == 0 || row == length - 1) {
            return fullWidth;
        }

        final int periodLength = 2 * (length - 1 - row);
        int betweenLength = row < rangeMiddle(length) ? periodLength : fullWidth - periodLength;

        int resultedPosition = status[row] ? fullWidth - betweenLength : betweenLength;
        status[row] = !status[row];
        return resultedPosition;
    }

}
