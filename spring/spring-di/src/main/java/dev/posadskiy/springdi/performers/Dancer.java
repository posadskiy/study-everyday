package dev.posadskiy.springdi.performers;

import dev.posadskiy.springdi.instruments.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Dancer implements Performer {

    @Autowired
    @Qualifier("dev.posadskiy.springdi.instruments.Guitar")
    private Instrument instrument;
    private String dance;

    public void perform() throws IllegalArgumentException {
        System.out.print("Dance: " + dance + " and listening: ");
        instrument.play();
    }
}
