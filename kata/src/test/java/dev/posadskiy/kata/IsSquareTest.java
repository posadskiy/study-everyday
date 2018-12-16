package dev.posadskiy.kata;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class IsSquareTest {
    @Test
    public void shouldWorkForSomeExamples() throws Exception {
        assertFalse("negative numbers aren't square numbers", IsSquare.isSquare(-1));
        assertTrue("0 is a square number (0 * 0)", IsSquare.isSquare(0));
        assertFalse("3 isn't a square number", IsSquare.isSquare(3));
        assertTrue("4 is a square number (2 * 2)", IsSquare.isSquare(4));
        assertTrue("25 is a square number (5 * 5)", IsSquare.isSquare(25));
        assertFalse("26 isn't a square number", IsSquare.isSquare(26));
    }
}