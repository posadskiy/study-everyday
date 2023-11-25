package com.posadskiy.kata;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import org.junit.Test;

public class FourSquaresTest {

    @Test
    public void Test1SmallInputs() {
        BigInteger[] result = new BigInteger[4];

/*        result = FourSquares.GetRepresentation(new BigInteger("0"));
        assertEquals(new BigInteger("0"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("1"));
        assertEquals(new BigInteger("1"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("17"));
        assertEquals(new BigInteger("17"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("33"));
        assertEquals(new BigInteger("33"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("215"));
        assertEquals(new BigInteger("215"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("333"));
        assertEquals(new BigInteger("333"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("4093"));
        assertEquals(new BigInteger("4093"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("1234567890"));
        assertEquals(new BigInteger("1234567890"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("34255235"));
        assertEquals(new BigInteger("34255235"), processResult(result));

        result = FourSquares.GetRepresentation(new BigInteger("106369249365575352836589875696130383747"));
        assertEquals(new BigInteger("106369249365575352836589875696130383747"), processResult(result));
   */     
        result = FourSquares.GetRepresentation(new BigInteger("269755285975747203035890901830824740960"));
        assertEquals(new BigInteger("269755285975747203035890901830824740960"), processResult(result));
    }

    private static final BigInteger processResult(BigInteger[] result) {
        BigInteger n = new BigInteger("0");
        BigInteger c = new BigInteger("1");

        c = new BigInteger("1");
        c = c.multiply(result[0]).multiply(result[0]);
        n = n.add(c);

        c = new BigInteger("1");
        c = c.multiply(result[1]).multiply(result[1]);
        n = n.add(c);

        c = new BigInteger("1");
        c = c.multiply(result[2]).multiply(result[2]);
        n = n.add(c);

        c = new BigInteger("1");
        c = c.multiply(result[3]).multiply(result[3]);
        n = n.add(c);

        return n;
    }
}
