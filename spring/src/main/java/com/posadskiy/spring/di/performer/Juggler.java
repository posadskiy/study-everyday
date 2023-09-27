package com.posadskiy.spring.di.performer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Juggler implements Performer {
    private int beanBags = 3;

    public Juggler() {
    }

    public Juggler(@Value("15") int beanBags) {
        this.beanBags = beanBags;
    }

    public void perform() throws IllegalArgumentException {
        System.out.println("JUGGLING " + beanBags + " BEANBAGS");
    }
}
