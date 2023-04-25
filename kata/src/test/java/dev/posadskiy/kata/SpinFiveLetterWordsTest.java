package dev.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpinFiveLetterWordsTest {
    @Test
    public void test() {
        assertEquals("emocleW", new SpinFiveLetterWords().spinWords("Welcome"));
        assertEquals("Hey wollef sroirraw", new SpinFiveLetterWords().spinWords("Hey fellow warriors"));
    }
}
