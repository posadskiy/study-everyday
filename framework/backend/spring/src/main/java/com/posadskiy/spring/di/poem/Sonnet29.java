package com.posadskiy.spring.di.poem;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class Sonnet29 implements Poem {
    private static final String[] LINES = {
        "My poems can be heard by you now," +
            "When the birch closes the firs," +
            "And we can see, without loss," +
            "How the heart trembles, barely audible."
    };

    public Sonnet29() {
    }

    public void recite() {
        for (String line : LINES) {
            log.info(line);
        }
    }
}
