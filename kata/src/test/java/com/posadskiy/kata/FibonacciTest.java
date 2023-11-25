package com.posadskiy.kata;

import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

public class FibonacciTest {
    @Test
    public void testFib0() {
        testFib(0, 0);
    }

    @Test
    public void testFib1() {
        testFib(1, 1);
    }

    @Test
    public void testFib2() {
        testFib(1, 2);
    }

    @Test
    public void testFib3() {
        testFib(2, 3);
    }

    @Test
    public void testFib4() {
        testFib(3, 4);
    }

    @Test
    public void testFib5() {
        testFib(5, 5);
    }

    @Test
    public void testFibMinus() {
        testFib(5, -5);
    }


    @Test
    public void testFibMinusBig() {
        testFib(44945570212853L, -67);
    }
    
    @Test
    public void testFib1000() {
        testFib("43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875", 1000);
    }
    
    

    @Test
    public void testFibRandom() throws ExecutionException, InterruptedException {
        final BigInteger fib = Fibonacci.fib(BigInteger.valueOf(20));

        final BigInteger multiply = Fibonacci.fib(BigInteger.valueOf(11)).multiply(Fibonacci.fib(BigInteger.valueOf(10)));
        final BigInteger multiply1 = Fibonacci.fib(BigInteger.valueOf(10)).multiply(Fibonacci.fib(BigInteger.valueOf(9)));
        final BigInteger add = multiply.add(multiply1);
    }

    @Test
    public void testFibBig() {
        testFib(44945570212853L, 1408688);
    }


    private static void testFib(long expected, long input) {
        BigInteger found;
        try {
            found = Fibonacci.fib(BigInteger.valueOf(input));
        } catch (Throwable e) {
            // see https://github.com/Codewars/codewars.com/issues/21
            throw new AssertionError("exception during test: " + e, e);
        }
        assertEquals(BigInteger.valueOf(expected), found);
    }

    private static void testFib(String expected, long input) {
        BigInteger found;
        try {
            found = Fibonacci.fib(BigInteger.valueOf(input));
        } catch (Throwable e) {
            // see https://github.com/Codewars/codewars.com/issues/21
            throw new AssertionError("exception during test: " + e, e);
        }
        assertEquals(new BigInteger(expected), found);
    }


}
