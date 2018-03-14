package dev.dposadsky.spring.poems;

import dev.dposadsky.spring.poems.Poem;

public class Sonnet29 implements Poem {
    private static String[] LINES = {
            "Стихи мои вам слышатся теперь",
            "Когда береза ели закрывает",
            "И можем мы увидеть без потерь",
            "Как сердце еле слышно трепетает"
    };

    public Sonnet29() {}

    public void recite() {
        for (String line : LINES) {
            System.out.println(line);
        }
    }
}
