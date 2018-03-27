package dev.posadskiy.springdi.performers;

import dev.posadskiy.springdi.instruments.Instrument;

import java.util.Collection;

public class OneManBand implements Performer {
    private Collection<Instrument> instruments;

    public void perform() throws IllegalArgumentException {
        for (Instrument instrument : instruments) {
            instrument.play();
        }
    }

    public void setInstruments(Collection<Instrument> instruments) {
        this.instruments = instruments;
    }
}
