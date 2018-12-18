package dev.posadskiy.kata;

import java.util.Stack;

/**
 * Write a function that takes a string of braces, and determines if the order of the braces is valid. It should return true if the string is valid, and false if it's invalid.
 *
 * This Kata is similar to the Valid Parentheses Kata, but introduces new characters: brackets [], and curly braces {}. Thanks to @arnedag for the idea!
 *
 * All input strings will be nonempty, and will only consist of parentheses, brackets and curly braces: ()[]{}.
 *
 * What is considered Valid?
 *
 * A string of braces is considered valid if all braces are matched with the correct brace.
 *
 * Examples
 *
 * "(){}[]"   =>  True
 * "([{}])"   =>  True
 * "(}"       =>  False
 * "[(])"     =>  False
 * "[({})](]" =>  False
 */
class BraceChecker {
    boolean isValid(String braces) {
        if (braces.length() % 2 == 1) return false;

        boolean isDeleted = true;
        String[] brace = new String[]{braces};
        while (isDeleted && brace[0].length() > 0) {
            String temp = brace[0].replace("{}", "").replace("()", "").replace("[]", "");
            isDeleted = temp.length() != brace[0].length();
            brace[0] = temp;
        }

        return brace[0].length() == 0;
    }

    public boolean isValidBetter(String braces) {
        Stack<Character> s = new Stack<>();
        for (char c : braces.toCharArray())
            if (s.size() > 0 && isClosing(s.peek(), c)) s.pop();
            else s.push(c);
        return s.size() == 0;
    }

    private boolean isClosing(char x, char c) {
        return (x == '{' && c == '}') || (x == '(' && c == ')') || (x == '[' && c == ']');
    }
}