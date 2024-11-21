package com.posadskiy.spring.di.performer;

import com.posadskiy.spring.di.instrument.Instrument;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Dancer implements Performer {

    private final Instrument instrument;

    @Value("Default dance")
    private String dance;

    public Dancer(@Qualifier("guitar") Instrument instrument) {
        this.instrument = instrument;
    }

    public void perform() throws IllegalArgumentException {
        System.out.print("Dance: " + dance + " and listening: ");
        instrument.play();
    }
}
