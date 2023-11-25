package com.posadskiy.spring.di.performer;

import com.posadskiy.spring.di.poem.Poem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Qualifier("poeticJuggler")
public class PoeticJuggler extends Juggler {

    private final Poem poem;

    public PoeticJuggler(Poem poem) {
        this.poem = poem;
    }

    public void perform() throws IllegalArgumentException {
        super.perform();
        log.info("Waiting for speech...");
        poem.recite();
    }
}
