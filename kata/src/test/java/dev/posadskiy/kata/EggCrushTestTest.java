package dev.posadskiy.kata;

import static java.math.BigInteger.valueOf;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class EggCrushTestTest {
    private static void test(int a, int b, int shouldBe) {
        assertEquals(valueOf(shouldBe), EggCrushTest.height(valueOf(a), valueOf(b)));
    }

    private static void test(String a, String b, String shouldBe) {
        assertEquals(new BigInteger(shouldBe), EggCrushTest.height(new BigInteger(a), new BigInteger(b)));
    }

    @org.junit.Test
    public void basicTests() throws Exception {
        test(1, 51, 51);
        test(2, 1, 1);
        test(4, 17, 3213);
        test(16, 19, 524096);
        test(23, 19, 524287);
    }

    @org.junit.Test
    public void advancedTests() throws Exception {
        test("13", "550", "60113767426276772744951355");
        test("271", "550", "1410385042520538326622498273346382708200418583791594039531058458108130216985983794998105636900856496701928202738750818606797013840207721579523618137220278767326000095");
        test("531", "550", "3685510180489786476798393145496356338786055879312930105836138965083617346086082863365358130056307390177215209990980317284932211552658342317904346433026688858140133147");
    }
}
