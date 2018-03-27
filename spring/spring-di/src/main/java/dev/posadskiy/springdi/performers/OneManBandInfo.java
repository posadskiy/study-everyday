package dev.posadskiy.springdi.performers;

import dev.posadskiy.springdi.instruments.Instrument;

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
