package com.posadskiy.kata;

import java.math.BigInteger;

/**
 * Consider a "word" as any sequence of capital letters A-Z (not limited to just "dictionary words"). For any word with at least two
 * different letters, there are other words composed of the same letters but in a different order (for instance, STATIONARILY/ANTIROYALIST,
 * which happen to both be dictionary words; for our purposes "AAIILNORSTTY" is also a "word" composed of the same letters as these two).
 * <p>
 * We can then assign a number to every word, based on where it falls in an alphabetically sorted list of all words made up of the same
 * group of letters. One way to do this would be to generate the entire list of words and find the desired one, but this would be slow if
 * the word is long.
 * <p>
 * Given a word, return its number. Your function should be able to accept any word 25 letters or less in length (possibly with some letters
 * repeated), and take no more than 500 milliseconds to run. To compare, when the solution code runs the 27 test cases in JS, it takes
 * 101ms.
 * <p>
 * For very large words, you'll run into number precision issues in JS (if the word's position is greater than 2^53). For the JS tests with
 * large positions, there's some leeway (.000000001%). If you feel like you're getting it right for the smaller ranks, and only failing by
 * rounding on the larger, submit a couple more times and see if it takes.
 * <p>
 * Python, Java and Haskell have arbitrary integer precision, so you must be precise in those languages (unless someone corrects me).
 * <p>
 * C# is using a long, which may not have the best precision, but the tests are locked so we can't change it.
 * <p>
 * Sample words, with their rank: ABAB = 2 AAAB = 1 BAAA = 4 QUESTION = 24572 BOOKKEEPER = 1074
 * <a href="https://www.codewars.com/kata/53e57dada0cb0400ba000688">Link</a>
 */
public class Anagrams {

    public BigInteger listPosition(String word) {
        if (word.equals("")) {
            return BigInteger.valueOf(1);
        }

        int[] letterCounts = new int[26];
        for (int i = 0; i < word.length(); i++) {
            letterCounts[word.charAt(i) - 'A']++;
        }

        char firstLetter = word.charAt(0);
        String current = word;
        BigInteger result = BigInteger.valueOf(0);
        for (int i = 1; i < current.length(); i++) {
            char c = current.charAt(i);
            if (c != '.' && c < firstLetter) {
                letterCounts[c - 'A']--;
                result = result.add(permutation(word.length() - 1, letterCounts));
                letterCounts[c - 'A']++;
                current = current.replace(c, '.');
            }
        }

        return result.add(listPosition(word.substring(1)));
    }

    private BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return result;
    }

    private BigInteger permutation(int n, int[] counts) {
        BigInteger result = factorial(n);
        for (int count : counts) {
            if (count < 2) {
                continue;
            }

            result = result.divide(factorial(count));
        }

        return result;
    }

}
