package com.posadskiy.spring.di.instrument;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("harmonica")
public class Harmonica implements Instrument {
    public void play() {
        System.out.println("TRUUU TUU TUUUU");
    }
}
