package dev.posadskiy.kata;

import static org.junit.Assert.*;

import org.junit.Test;

public class NextNumberWithSameDigitsTest {
    @Test
    public void basicTests() {
        assertEquals(21, NextNumberWithSameDigits.nextBiggerNumber(12));
        assertEquals(531, NextNumberWithSameDigits.nextBiggerNumber(513));
        assertEquals(2071, NextNumberWithSameDigits.nextBiggerNumber(2017));
        assertEquals(441, NextNumberWithSameDigits.nextBiggerNumber(414));
        assertEquals(414, NextNumberWithSameDigits.nextBiggerNumber(144));
        assertEquals(19009, NextNumberWithSameDigits.nextBiggerNumber(10990));
        assertEquals(9876543210L, NextNumberWithSameDigits.nextBiggerNumber(9876543201L));
        assertEquals(1234567908, NextNumberWithSameDigits.nextBiggerNumber(1234567890));
        assertEquals(674132426, NextNumberWithSameDigits.nextBiggerNumber(674132264));
        assertEquals(911111111111111118L, NextNumberWithSameDigits.nextBiggerNumber(891111111111111111L));
        assertEquals(911111111111111118L, NextNumberWithSameDigits.nextBiggerNumberBest(891111111111111111L));
    }
}
