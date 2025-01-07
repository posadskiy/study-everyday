package com.posadskiy.java.release.v22.unnamedVariables;

import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * JEP 456: Unnamed Variables & Patterns
 * <p>
 * Enhance the Java programming language with unnamed variables and unnamed patterns, which can be used when variable
 * declarations or nested patterns are required but never used. Both are denoted by the underscore character, _.
 * <p>
 * <a href="https://openjdk.org/jeps/456">Docs</a>
 */
@Log4j2
public class UnnamedVariables {
    public static void main(String[] args) {
        // transaction IDs
        var transactions = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        var count = UnnamedVariables.countTransactions(transactions);
        log.info("Transactions count is {}", count);
    }

    // Could be replaced with transactions.size(); 
    public static int countTransactions(List<Integer> transactions) {
        var total = 0;
        for (var _ : transactions) {
            total++;
        }

        return total;
    }
}
