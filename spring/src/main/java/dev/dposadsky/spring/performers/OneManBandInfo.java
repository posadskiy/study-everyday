package dev.dposadsky.spring.performers;

import dev.dposadsky.spring.instruments.Instrument;

import java.util.Map;

public class OneManBandInfo implements Performer {
    private Map<String, Instrument> instruments;

    public void perform() throws IllegalArgumentException {
        for (String key : instruments.keySet()) {
            System.out.print(key + ": ");
            Instrument instrument = instruments.get(key);
            instrument.play();
        }
    }

    public void setInstruments(Map<String, Instrument> instruments) {
        this.instruments = instruments;
    }
}
