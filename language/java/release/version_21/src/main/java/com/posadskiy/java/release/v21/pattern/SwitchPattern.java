package com.posadskiy.java.release.v21.pattern;

import lombok.extern.log4j.Log4j2;

/**
 * JEP 441: Pattern Matching for switch
 * <a href="https://openjdk.org/jeps/441">Docs</a>
 * <p>
 * Enhance the Java programming language with pattern matching for switch expressions and statements. Extending pattern
 * matching to switch allows an expression to be tested against a number of patterns, each with a specific action, so
 * that complex data-oriented queries can be expressed concisely and safely.
 */
@Log4j2
public class SwitchPattern {

    public static void main(String[] args) {
        final SwitchPattern switchPattern = new SwitchPattern();

        switchPattern.printValue(1);
        switchPattern.printValue(200);
        switchPattern.printValue(1.2);
        switchPattern.printValue(2L);
        switchPattern.printValue("3");
    }

    public void printValue(Object value) {
        var formatterValue = switch (value) {
            case Integer i
                when i < 128 -> String.format("byte %d", i);
            case Integer i
                when i > 128 -> String.format("int %d", i);
            case Long l -> String.format("long %d", l);
            case Double d -> String.format("double %f", d);
            case String s -> String.format("String %s", s);
            case null, default -> "Unexpected expression";
        };

        log.info(formatterValue);
    }
}
