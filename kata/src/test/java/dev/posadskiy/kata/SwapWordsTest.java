package dev.posadskiy.kata;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SwapWordsTest {

    @Test
    public void testWords() {
        char[] source = new char[]{'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'd', 'o', 'g'};
        char[] result = new char[]{'d', 'o', 'g', ' ', 'a', ' ', 'i', 's', ' ', 't', 'h', 'i', 's'};
        SwapWords words = new SwapWords();

        words.words(source);

        assertEquals(source, result);
    }

    @Test
    public void testWordsHipo() {
        char[] source = new char[]{'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'h', 'i', 'p', 'o', 'p', 'o', 't', 'a', 'm'};
        char[] result = new char[]{'h', 'i', 'p', 'o', 'p', 'o', 't', 'a', 'm', ' ', 'a', ' ', 'i', 's', ' ', 't', 'h', 'i', 's'};
        SwapWords words = new SwapWords();

        words.words(source);

        assertEquals(source, result);
    }
}