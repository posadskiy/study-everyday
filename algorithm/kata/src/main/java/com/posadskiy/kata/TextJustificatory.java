package com.posadskiy.kata;

/**
 * Your task in this Kata is to emulate text justification in monospace font. You will be given a single-lined text and the expected justification width. The longest word will never be greater than this width.
 *
 * Here are the rules:
 *
 * Use spaces to fill in the gaps between words.
 * Each line should contain as many words as possible.
 * Use '\n' to separate lines.
 * Gap between words can't differ by more than one space.
 * Lines should end with a word not a space.
 * '\n' is not included in the length of a line.
 * Large gaps go first, then smaller ones ('Lorem--ipsum--dolor--sit-amet,' (2, 2, 2, 1 spaces)).
 * Last line should not be justified, use only one space between words.
 * Last line should not contain '\n'
 * Strings with one word do not need gaps ('somelongword\n').
 * 
 * Link: https://www.codewars.com/kata/537e18b6147aa838f600001b/java
 */
public class TextJustificatory {
    public static String justify(String text, int width) {
        final String[] source = text.split(" ");

        StringBuilder result = new StringBuilder();
        StringBuilder string = new StringBuilder();
        for (String word : source) {
            if (word.length() + string.length() + 1 > width) {
                result.append(
                    addJustified(string, width)
                )
                    .append("\n");
                string
                    .delete(0, string.length());
            }

            if (string.length() != 0) string.append(" ");
            
            string.append(word);
        }
        
        result.append(
            string
        );
        
        return result.toString();
    }
    
    private static String addJustified(StringBuilder raw, int width) {
        final String[] words = raw.toString().split(" ");
        if (words.length == 0) return "";
        if (words.length == 1) return words[0];

        final int freeSpaces = width - raw.length();
        final int additionalSpacesPerWord = freeSpaces / (words.length - 1);
        final int additionalSpacesForFirstWords = freeSpaces % (words.length - 1);

        StringBuilder result = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            result.append(" ");
            if (i <= additionalSpacesForFirstWords) result.append(" ");

            for (int i1 = 0; i1 < additionalSpacesPerWord; i1++) {
                result.append(" ");
            }
            
            if (i != words.length - 1) result.append(words[i]);
        }
        
        result.append(words[words.length - 1]);

        return result.toString();
    }
}
