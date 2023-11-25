package com.posadskiy.spring.di.instrument;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Qualifier("saxophone")
public class Saxophone implements Instrument {
    public void play() {
        log.info("TOOT TOT TOOOT");
    }
}
