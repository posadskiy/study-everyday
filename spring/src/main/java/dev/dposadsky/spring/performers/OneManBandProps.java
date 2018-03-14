package dev.dposadsky.spring.performers;

import java.util.Properties;

public class OneManBandProps implements Performer {
    private Properties instruments;

    public void perform() throws IllegalArgumentException {
        for (Object instrument : instruments.keySet()) {
            System.out.println(instrument + ": " + instruments.getProperty((String) instrument));
        }
    }

    public void setInstruments(Properties instruments) {
        this.instruments = instruments;
    }
}
