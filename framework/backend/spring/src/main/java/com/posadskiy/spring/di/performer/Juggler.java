package com.posadskiy.spring.di.performer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class Juggler implements Performer {
    private int beanBags = 3;

    public Juggler() {
    }

    public Juggler(@Value("15") int beanBags) {
        this.beanBags = beanBags;
    }

    public void perform() throws IllegalArgumentException {
        log.info("JUGGLING " + beanBags + " BEANBAGS");
    }
}
