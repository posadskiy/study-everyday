package com.posadskiy.spring.di.performer;

import com.posadskiy.spring.di.poem.Poem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("poeticJuggler")
public class PoeticJuggler extends Juggler {

    private final Poem poem;

    public PoeticJuggler(Poem poem) {
        this.poem = poem;
    }

    public void perform() throws IllegalArgumentException {
        super.perform();
        System.out.println("Waiting for speech...");
        poem.recite();
    }
}
