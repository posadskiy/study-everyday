package com.posadskiy.design.pattern.behavioural.chainofresponsibility;

import java.util.Optional;

public abstract class Rule {
    private Rule next;

    public static Optional<Rule> add(Rule... rules) {
        if (rules == null || rules.length == 0) {
            return Optional.empty();
        }
        
        var head = rules[0];
        for (Rule rule : rules) {
            if (rule != null) {
                head.next = rule;
                head = head.next;
            }
        }
        
        return Optional.ofNullable(rules[0]);
    }

    public abstract boolean check(Sensor sensor);

    protected boolean checkNext(Sensor sensor) {
        return next.check(sensor);
    }
}
