package com.posadskiy.spring.di.performer;

import com.posadskiy.spring.di.instrument.Instrument;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
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
        log.info("Playing " + song + ": ");
        instrument.play();
    }
}
