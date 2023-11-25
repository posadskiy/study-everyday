package com.posadskiy.java.release.v10;

import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * JEP 286: Local-Variable Type Inference
 * <a href="https://openjdk.org/jeps/286">Docs</a>
 */
@Log4j2
public class LocalVariableTypeInference {

    public static void main(String[] args) {
        var value = List.of(1, 2, 3);

        log.info(value.size());
    }

}
