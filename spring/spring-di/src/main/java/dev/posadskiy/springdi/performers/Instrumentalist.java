package dev.posadskiy.springdi.performers;

import dev.posadskiy.springdi.instruments.Instrument;

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
