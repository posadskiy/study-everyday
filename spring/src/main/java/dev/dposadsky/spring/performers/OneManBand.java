package dev.dposadsky.spring.performers;

import dev.dposadsky.spring.instruments.Instrument;
import dev.dposadsky.spring.performers.Performer;

import java.util.Collection;

public class OneManBand implements Performer {
    Collection<Instrument> instruments;

    public void perform() throws IllegalArgumentException {
        for (Instrument instrument : instruments) {
            instrument.play();
        }
    }

    public void setInstruments(Collection<Instrument> instruments) {
        this.instruments = instruments;
    }
}
