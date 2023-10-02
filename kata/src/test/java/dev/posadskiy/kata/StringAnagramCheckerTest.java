package dev.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringAnagramCheckerTest {
    @Test
    public void test() {
        final StringAnagramChecker stringAnagramChecker = new StringAnagramChecker();
        assertTrue(stringAnagramChecker.isAnagram("anagram", "nagaram"));
    }

}
