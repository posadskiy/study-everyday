package com.posadskiy.java.release.v14;

import lombok.extern.log4j.Log4j2;

/**
 * JEP 358: Helpful NullPointerExceptions
 * <a href="https://openjdk.org/jeps/358">Docs</a>
 */
@Log4j2
public class HelpfulNullPointerExceptions {

    public static void main(String[] args) {
        First first = null;
        log.info(first.second.third.field);
    }

    class First {
        Second second;
    }

    class Second {
        Third third;
    }

    class Third {
        String field;
    }
}
