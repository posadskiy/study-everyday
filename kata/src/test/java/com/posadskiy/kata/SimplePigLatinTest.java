package com.posadskiy.kata;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimplePigLatinTest {

    @Test
    public void FixedTests() {
        assertEquals("igPay atinlay siay oolcay", SimplePigLatin.pigIt("Pig latin is cool"));
        assertEquals("hisTay siay ymay tringsay", SimplePigLatin.pigIt("This is my string"));
    }

    @Test
    public void FixedTestsBest() {
        assertEquals("igPay atinlay siay oolcay", SimplePigLatin.pigItBest("Pig latin is cool"));
        assertEquals("hisTay siay ymay tringsay", SimplePigLatin.pigItBest("This is my string"));
    }
}
