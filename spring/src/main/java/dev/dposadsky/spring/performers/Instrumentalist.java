package dev.dposadsky.spring.performers;

import dev.dposadsky.spring.instruments.Instrument;
import dev.dposadsky.spring.performers.Performer;

public class Instrumentalist implements Performer {

    private Instrument instrument;
    private String song;

    public Instrumentalist() {}

    public void perform() throws IllegalArgumentException {
        System.out.println("Playing " + song + ": ");
        instrument.play();
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getSong() {
        return song;
    }

    public Instrument getInstrument() {
        return instrument;
    }

}
