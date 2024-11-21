package com.posadskiy.kata;

import org.apache.commons.lang3.StringUtils;

public class Accumul {
    public static String accum(String source) {
        StringBuilder result = new StringBuilder();
        char[] chars = source.toLowerCase().toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            result.append(Character.toUpperCase(chars[i])).append(StringUtils.repeat(chars[i], i));
            if (i != chars.length - 1) {
                result.append('-');
            }
        }

        return result.toString();
    }
}
