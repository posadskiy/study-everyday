package com.posadskiy.spring.di.performer;

import com.posadskiy.spring.di.instrument.Instrument;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class Instrumentalist implements Performer {

    private final Instrument instrument;

    @Value("Jingle Bells")
    @Setter
    private String song;

    public Instrumentalist(@Qualifier("guitar") Instrument instrument) {
        this.instrument = instrument;
    }

    public void perform() throws IllegalArgumentException {
        System.out.println("Playing " + song + ": ");
        instrument.play();
    }
}
