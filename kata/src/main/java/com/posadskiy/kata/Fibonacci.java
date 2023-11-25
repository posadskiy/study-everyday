package com.posadskiy.kata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * n this kata you will have to calculate fib(n) where:
 * <p>
 * fib(0) := 0
 * fib(1) := 1
 * fib(n + 2) := fib(n + 1) + fib(n)
 * Write an algorithm that can handle n up to 2000000.
 * <p>
 * Your algorithm must output the exact integer answer, to full precision. Also, it must correctly handle negative numbers as input.
 * <p>
 * HINT I: Can you rearrange the equation fib(n + 2) = fib(n + 1) + fib(n) to find fib(n) if you already know fib(n + 1) and fib(n + 2)? Use this to reason what value fib has to have for negative values.
 * <p>
 * HINT II: See <a href="https://web.archive.org/web/20220614001843/https://mitpress.mit.edu/sites/default/files/sicp/full-text/book/book-Z-H-11.html#%_sec_1.2.4">Hint</a>
 */
public class Fibonacci {
    private static final List<Long> points = new ArrayList<>();
    private static final List<Long> futureCalculation = new ArrayList<>();
    private static final Map<Long, BigInteger> result = new HashMap<>();

    public static BigInteger fib(BigInteger n) {
        final long number = n.longValue();
        if (number == 0) return BigInteger.ZERO;
        if (number == 1) return BigInteger.ONE;

        if (number > 0) {
            calculatePoints(number);

            while (futureCalculation.size() != 0) {
                final Long future = futureCalculation.get(0);
                futureCalculation.remove(0);
                calculatePoints(future);
                final List<Long> collect = futureCalculation.stream().distinct().collect(Collectors.toList());
                futureCalculation.clear();
                futureCalculation.addAll(collect);
            }

            final List<Long> collect = points.stream().sorted().distinct().collect(Collectors.toList());
            points.clear();
            points.addAll(collect);

            for (Long point : points) {
                long first;
                long second;
                if (point % 2 == 1) {
                    first = point / 2;
                    second = first + 1;
                } else {
                    first = second = point / 2;
                }

                if (first < 30) {
                    result.put(first, BigInteger.valueOf(nthFibonacciTerm(first)));
                    result.put(first + 1, BigInteger.valueOf(nthFibonacciTerm(first + 1)));
                    result.put(second, BigInteger.valueOf(nthFibonacciTerm(second)));
                    result.put(second - 1, BigInteger.valueOf(nthFibonacciTerm(second - 1)));
                }

                final BigInteger fn = result.get(first);
                final BigInteger fn1 = result.get(first + 1);
                final BigInteger fk = result.get(second);
                final BigInteger fk1 = result.get(second - 1);
                final BigInteger left = fn1.multiply(fk);
                final BigInteger right = fn.multiply(fk1);

                result.put(point, left.add(right));
            }

            return result.get(number);

        }

        BigInteger current;
        long counter;
        counter = -2;
        current = BigInteger.valueOf(-1);
        BigInteger previous = BigInteger.valueOf(-1);
        while (counter > number) {
            BigInteger temp = current;
            current = current.add(previous);
            previous = temp;
            counter--;
        }

        if (number % 2 != 0) {
            current = current.multiply(BigInteger.valueOf(-1));
        }

        return current;
    }

    public static int nthFibonacciTerm(long n) {
        double squareRootOf5 = Math.sqrt(5);
        double phi = (1 + squareRootOf5) / 2;
        return (int) ((Math.pow(phi, n) - Math.pow(-phi, -n)) / squareRootOf5);
    }

    public static List<Long> calculatePoints(long number) {
        points.add(number);
        long start = (number % 2 == 1) ? number - 1 : number;

        long up;
        long middle = start;
        long down;
        points.add(middle);
        while (middle > 10) {
            middle = middle / 2;
            if (middle % 2 == 1) {
                if (!futureCalculation.contains(middle + 1)) {
                    futureCalculation.add(middle + 1);
                }
                middle--;
            }
            up = middle + 1;
            down = middle - 1;

            points.add(down);
            points.add(middle);
            points.add(up);
        }

        return points;
    }
}
