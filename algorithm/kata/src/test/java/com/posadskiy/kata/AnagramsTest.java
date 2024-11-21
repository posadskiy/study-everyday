package com.posadskiy.kata;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class  AnagramsTest {
    @Test
    public void testKnownInputs() {
        Anagrams anagram = new Anagrams();

        // QUESTION
        // EINOQSTU
        
        // 58167243
        // 12345678
        // (4,6,0,2,2,0,0,0)

        // 4*7! + 6*6! + 0*5! + 3*4! + 3*3! + 1
        // 24541 != 24572
        
        // 3 5 -1 1 1 -3 -2 -4
        
        
        // BOOKKEEPER
        // BEEEKKOOPR
        
        // 
        // 12345678910
        // (0, 5, 5, 1, 1, 0, 0, 1, 0, 0)
        
        // 0*9! + 5*8!/2!/3!/2! + 5*7!/3!/2! + 1*6!/3!/2! + 1*5!/3! + 0*4! + 0*3! + 1*2! + 0*1! + 1

        /**
         * aabb
         * abab
         * abba
         * baab
         * baba
         * bbaa
         */

        /**
         * aabc
         * aacb
         * abac
         * abca
         * acab
         * acba
         * b
         * 
         */
        
        // 1*3!/2! + 1
        // 1*3!/2! + 1*2! + 1
        // 1*2!

        assertEquals("Position for 'A' incorrect", BigInteger.ONE, anagram.listPosition("QUESTION"));
        assertEquals("Position for 'ABAB' incorrect", BigInteger.valueOf(2), anagram.listPosition("ABAB"));
        assertEquals("Position for 'AAAB' incorrect", BigInteger.ONE, anagram.listPosition("AAAB"));
        assertEquals("Position for 'BAAA' incorrect", BigInteger.valueOf(4), anagram.listPosition("BAAA"));
        assertEquals("Position for 'BBAA' incorrect", BigInteger.valueOf(6), anagram.listPosition("BBAA"));
        assertEquals("Position for 'BAAB' incorrect", BigInteger.valueOf(4), anagram.listPosition("BAAB"));
        assertEquals("Position for 'QUESTION' incorrect", BigInteger.valueOf(24572), anagram.listPosition("QUESTION"));
        assertEquals("Position for 'BOOKKEEPER' incorrect", BigInteger.valueOf(10743), anagram.listPosition("BOOKKEEPER"));
    }

}
