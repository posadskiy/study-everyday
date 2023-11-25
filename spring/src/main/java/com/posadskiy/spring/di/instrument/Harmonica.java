package com.posadskiy.spring.di.instrument;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Qualifier("harmonica")
public class Harmonica implements Instrument {
    public void play() {
        log.info("TRUUU TUU TUUUU");
    }
}
