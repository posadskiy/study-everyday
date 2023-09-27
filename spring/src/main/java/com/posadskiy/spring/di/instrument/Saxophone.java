package com.posadskiy.spring.di.instrument;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("saxophone")
public class Saxophone implements Instrument {
    public void play() {
        System.out.println("TOOT TOT TOOOT");
    }
}
