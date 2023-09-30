package dev.posadskiy.kata;

import java.math.BigInteger;

public class EggCrushTest {

    public static BigInteger height(BigInteger n, BigInteger m) {
        var answer = BigInteger.ZERO;
        var r = BigInteger.ONE;
        for (BigInteger i = BigInteger.ONE; i.compareTo(n.add(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
            r = r.multiply(m.add(BigInteger.ONE).subtract(i)).divide(i);
            answer = answer.add(r);
        }
        return answer;
    }

}
