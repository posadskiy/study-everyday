package com.posadskiy.kata;

import org.testng.annotations.Test;

import static org.junit.Assert.assertArrayEquals;


public class WhichAreInTest {

    @Test
    public void test1() {
        String a[] = new String[]{ "arp", "live", "strong" };
        String b[] = new String[] { "lively", "alive", "harp", "sharp", "armstrong" };
        String r[] = new String[] { "arp", "live", "strong" };
        assertArrayEquals(r, WhichAreIn.inArray(a, b));
    }
}
