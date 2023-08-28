package dev.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SumStringsAsNumbersTest {

    @Test
    public void test() {
        assertEquals("579", SumStringsAsNumbers.sumStrings("123", "456"));
    }
}
