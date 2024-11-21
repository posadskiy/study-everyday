package com.posadskiy.kata;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Move the first letter of each word to the end of it, then add "ay" to the end of the word. Leave punctuation marks untouched.
 * <p>
 * Examples
 * <p>
 * pigIt('Pig latin is cool'); // igPay atinlay siay oolcay
 * pigIt('Hello world !');     // elloHay orldway !
 */
public class SimplePigLatin {
    public static String pigIt(String str) {
        return Arrays.stream(str.split(" "))
            .map(word -> {
                if (!StringUtils.isAlpha(word)) return word;
                
                return word.substring(1) + word.charAt(0) + "ay";
            }).collect(Collectors.joining(" "));
    }
    
    public static String pigItBest(String str) {
        return str.replaceAll("(\\w)(\\w*)", "$2$1ay");
    }
}
