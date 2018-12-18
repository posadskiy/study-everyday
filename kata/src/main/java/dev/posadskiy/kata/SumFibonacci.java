package dev.posadskiy.kata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * The drawing shows 6 squares the sides of which have a length of 1, 1, 2, 3, 5, 8. It's easy to see that the sum of the perimeters of these squares is : 4 * (1 + 1 + 2 + 3 + 5 + 8) = 4 * 20 = 80
 *
 * Could you give the sum of the perimeters of all the squares in a rectangle when there are n + 1 squares disposed in the same manner as in the drawing:
 *
 * The function perimeter has for parameter n where n + 1 is the number of squares (they are numbered from 0 to n) and returns the total perimeter of all the squares.
 *
 * perimeter(5)  should return 80
 * perimeter(7)  should return 216
 */
class SumFibonacci {

    static BigInteger perimeter(BigInteger n) {
        if (n.equals(BigInteger.ZERO)) return BigInteger.valueOf(4);
        if (n.equals(BigInteger.ONE)) return BigInteger.valueOf(8);

        List<BigInteger> fibonacci = new ArrayList<>();
        fibonacci.add(BigInteger.ONE);
        fibonacci.add(BigInteger.ONE);

        int i = 2;

        while (i <= n.intValue()) {
            fibonacci.add(fibonacci.get(i-1).add(fibonacci.get(i-2)));
            ++i;
        }

        return BigInteger.valueOf(4).multiply(fibonacci.stream().reduce(BigInteger::add).get());
    }
}
