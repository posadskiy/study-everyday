package dev.posadskiy.kata;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Test;

public class SumFibonacciTest {

    @Test
    public void test1() {
        assertEquals(BigInteger.valueOf(80), SumFibonacci.perimeter(BigInteger.valueOf(5)));
    }
    @Test
    public void test2() {
        assertEquals(BigInteger.valueOf(216), SumFibonacci.perimeter(BigInteger.valueOf(7)));
    }
    @Test
    public void test3() {
        assertEquals(BigInteger.valueOf(14098308), SumFibonacci.perimeter(BigInteger.valueOf(30)));
    }

}