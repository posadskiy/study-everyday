package com.posadskiy.spring.di.instrument;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("guitar")
public class Guitar implements Instrument {
    public void play() {
        System.out.println("BRING BRING BRIING");
    }
}
