package dev.posadskiy.kata;

import org.junit.Test;

import static org.testng.Assert.*;

public class CountOfSymbolsTest {

    @Test
    public void count_4CountInput_4CountReturned() {
        String a = "ab";
        String b = "aabbcdd";

        int result = CountOfSymbols.count(a, b);

        assertEquals(result, 4);
    }

}