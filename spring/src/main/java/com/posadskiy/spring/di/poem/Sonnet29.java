package com.posadskiy.spring.di.poem;

import org.springframework.stereotype.Service;

@Service
public class Sonnet29 implements Poem {
    private static final String[] LINES = {
        "Стихи мои вам слышатся теперь",
        "Когда береза ели закрывает",
        "И можем мы увидеть без потерь",
        "Как сердце еле слышно трепетает"
    };

    public Sonnet29() {
    }

    public void recite() {
        for (String line : LINES) {
            System.out.println(line);
        }
    }
}
