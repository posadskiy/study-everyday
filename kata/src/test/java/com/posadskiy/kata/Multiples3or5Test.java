package com.posadskiy.kata;

import org.junit.Test;

import static org.testng.Assert.*;

public class Multiples3or5Test {

    @Test
    public void test() {
        assertEquals(23, new Multiples3or5().solution(10));
    }

}
