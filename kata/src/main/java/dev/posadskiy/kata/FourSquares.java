package dev.posadskiy.kata;

import java.math.BigInteger;

public class FourSquares {

    private BigInteger number; // the number we are trying to break up
    private int count;
    private BigInteger[] terms;

    /*
     * largestSquare - a private helper method that returns the largest perfect
     * square less than or equal to the specified positive integer n.
     */
    private static BigInteger largestSquare(BigInteger n) {
        BigInteger sqrt = n.sqrt();
        return sqrt.pow(2);
    }

    /**
     * constructor
     */
    public FourSquares(BigInteger num, int count) {
        this.number = num;
        this.terms = new BigInteger[count];
        this.count = count;
    }

    /**
     * findSum method - a "wrapper" method that makes the initial call to the key recursive-backtracking method (the findSumRB method found
     * below), and that returns the same value that it returns.
     */
    public boolean findSum(BigInteger num) {
        return findSumRB(num, count);
    }

    /**
     * findSumRB method - the key recursive-backtracking method. We call it to break the specified number (num) into a sum of at most
     * maxTerms perfect squares. Returns true if the solution has been found and false otherwise.
     */
    public boolean findSumRB(BigInteger num, int maxTerms) {
        int number = 0;
        if (num.compareTo(BigInteger.ZERO) == 0) {
            return true;
        }

        if (maxTerms == 0) {
            return false;
        }

        BigInteger sum = largestSquare(num);

        this.terms[count - (maxTerms)] = sum;
        // System.out.println("" + Arrays.toString(this.terms));

        num = num.subtract(this.terms[count - (maxTerms)]);
        if (this.findSumRB((num), --maxTerms)) { // recursive call (backtrack)
            if (num.compareTo(BigInteger.ZERO) == 0) {
                for (int i = 0; i < maxTerms; ++i) {
                    this.terms[count - i - 1] = BigInteger.ZERO;
                }
            }
            return true;
        } else {
            while (!calculateSq(sum, maxTerms)) {
                if (maxTerms != 4 || number++ > 100) return false;
                if (sum.compareTo(BigInteger.ONE) <= 0) {
                    return false;
                }
                sum = largestSquare(sum.subtract(BigInteger.ONE));
            }
            return true;
        }
    }

    public boolean calculateSq(BigInteger sum, int maxTerms) {
        this.terms[count - (maxTerms) - 1] = sum;
        // System.out.println("" + Arrays.toString(this.terms));
        BigInteger num1 = BigInteger.ZERO;
        for (int i = 0; i < (count - maxTerms); i++) {
            num1 = num1.add(this.terms[i]);
        }
        num1 = number.subtract(num1);

        return this.findSumRB((num1), maxTerms);
    }


    public static BigInteger[] GetRepresentation(BigInteger n) {
        FourSquares problem = new FourSquares(n, 4);

        var b = problem.findSum(n);
        if (!b) {
            problem = new FourSquares(n, 3);
            problem.findSum(n);
        }
        BigInteger[] result = new BigInteger[4];
        for (int i = 0; i < 4; i++) {
            if (problem.terms.length > i && problem.terms[i] != null) {
                result[i] = problem.terms[i].sqrt();
            } else {
                result[i] = BigInteger.ZERO;
            }
        }
        return result;
    }

}
