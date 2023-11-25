package com.posadskiy.java.release.v14;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SwitchExpressions {

    public static void main(String[] args) {
        int value = 1;
        switch (value) {
            case 1, 2, 3 -> log.info("One two or three");
            case 4, 5 -> log.info("Four or five");
            case 6 -> log.info("Six");
        }

        int result = switch (value) {
            case 1, 2, 3 -> 0;
            case 4, 5 -> 1;
            case 6 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };

        log.info(result);

        int response = switch (value) {
            case 1, 2, 3 -> {
                log.info("Value is " + value);
                yield 0;
            }
            case 4, 5 -> 1;
            case 6 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };

        log.info(response);
    }

}
