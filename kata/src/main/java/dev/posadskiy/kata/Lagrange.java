package dev.posadskiy.kata;

import java.util.Scanner;

public class Lagrange {
    private int number; // the number we are trying to break up
    private int[] terms;

    /*
     * largestSquare - a private helper method that returns the largest perfect
     * square less than or equal to the specified positive integer n.
     */
    private static int largestSquare(int n) {
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt;
    }

    /**
     * constructor
     */
    public Lagrange(int num) {
        this.number = num;
        this.terms = new int[4];
    }

    /**
     * printSolution - print the solution to the problem
     */
    public void printSolution() {
        System.out.print(this.number + " = ");

        for (int i = 0; i < terms.length; i++) {
            if (terms[i] != 0) {
                if(i!=0) {
                    System.out.print(" + ");
                }
                System.out.print(terms[i]);
            }

        }

    }

    /**
     * findSum method - a "wrapper" method that makes the initial call to the key
     * recursive-backtracking method (the findSumRB method found below), and that
     * returns the same value that it returns.
     */
    public boolean findSum(int num) {
        return findSumRB(num, 4);
    }

    /**
     * findSumRB method - the key recursive-backtracking method. We call it to break
     * the specified number (num) into a sum of at most maxTerms perfect squares.
     * Returns true if the solution has been found and false otherwise.
     */
    public boolean findSumRB(int num, int maxTerms) {

        if (num == 0) {
            return true;
        }

        if (maxTerms == 0) {
            return false;
        }

        int sum = largestSquare(num);

        this.terms[4 - (maxTerms)] = sum;
        // System.out.println("" + Arrays.toString(this.terms));

        num = num - this.terms[4 - (maxTerms)];
        if (this.findSumRB((num), --maxTerms)) { // recursive call (backtrack)
            return true;
        } else {
            sum = largestSquare(sum - 1);
            if (sum <= 1) {
                return false;
            } else {
                this.terms[4 - (maxTerms) - 1] = sum;
                // System.out.println("" + Arrays.toString(this.terms));
                int num1 = 0;
                for (int i = 0; i < (4 - maxTerms); i++) {
                    num1 = num1 + this.terms[i];
                }
                num1 = number - num1;

                return this.findSumRB((num1), maxTerms);
            }
        }
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a positive integer (-1 to quit): ");
            int n = console.nextInt();
            console.nextLine();

            if (n == -1) {
                System.out.println("Goodbye!");
                return;
            } else if (n <= 0) {
                continue;
            }

            Lagrange problem = new Lagrange(n);

            if (problem.findSum(n)) {
                problem.printSolution();
            } else {
                System.out.println("could not find a sum for " + n);
                System.out.println();
            }
            System.out.println();
        }
    }
}
