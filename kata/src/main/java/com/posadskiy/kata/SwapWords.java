package com.posadskiy.kata;

public class SwapWords {
    public char[] words(char[] source) {
        if (source == null) throw new IllegalArgumentException("Empty array");

        int current = 0;
        int lastSpace = source.length;

        int length = source.length - 1;

        while (current+1 != lastSpace) {
            int i = source.length - 1;
            int size = 0;
            while (i > current) {
                if (source[i] == ' ') {
                    lastSpace = i;
                    size = source.length - i;
                    break;
                }
                --i;
            }

            for (int j = length; j > lastSpace; --j) {
                char temp = source[length];
                if (source.length - 1 - current >= 0)
                    System.arraycopy(source, current, source, current + 1, source.length - 1 - current);
                source[current] = temp;
            }

            current += size;
            if (source.length - current >= 0)
                System.arraycopy(source, current - 1, source, current, source.length - current);
            source[current - 1] = ' ';
        }

        return source;
    }
}
