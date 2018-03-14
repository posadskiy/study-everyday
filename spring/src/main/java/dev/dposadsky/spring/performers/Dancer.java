package dev.dposadsky.spring.performers;

import dev.dposadsky.spring.instruments.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Dancer implements Performer {

    @Autowired
    @Qualifier("dev.dposadsky.spring.instruments.Guitar")
    private Instrument instrument;
    private String dance;

    public void perform() throws IllegalArgumentException {
        System.out.print("Dance: " + dance + " and listening: ");
        instrument.play();
    }
}
