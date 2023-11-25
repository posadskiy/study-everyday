package com.posadskiy.kata;


/**
 * Task
 *
 * Given an integral number, determine if it's a square number:
 *
 * In mathematics, a square number or perfect square is an integer that is the square of an integer; in other words, it is the product of some integer with itself.
 * The tests will always use some integral number, so don't worry about that in dynamic typed languages.
 *
 * Examples
 *
 * is_square (-1) # => false
 * is_square   0 # => true
 * is_square   3 # => false
 * is_square   4 # => true
 * is_square  25 # => true
 * is_square  26 # => false
 * isSquare(-1) returns  false
 * isSquare(0) returns   true
 * isSquare(3) returns   false
 * isSquare(4) returns   true
 * isSquare(25) returns  true
 * isSquare(26) returns  false
 */
public class IsSquare {

    public static boolean isSquare(int n) {
        return Math.sqrt(n) % 1 == 0;
    }
}
