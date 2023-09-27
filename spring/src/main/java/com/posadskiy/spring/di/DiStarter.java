package com.posadskiy.spring.di;

import com.posadskiy.spring.di.performer.Performer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DiStarter {

    private final Performer poeticJuggler;
    private final Performer instrumentalist;
    private final Performer dancer;

    public DiStarter(@Qualifier("poeticJuggler") Performer poeticJuggler, @Qualifier("instrumentalist") Performer instrumentalist,
                     @Qualifier("dancer") Performer dancer) {
        this.poeticJuggler = poeticJuggler;
        this.instrumentalist = instrumentalist;
        this.dancer = dancer;
    }

    public void start() {
        poeticJuggler.perform();
        instrumentalist.perform();

        dancer.perform();
    }

}
