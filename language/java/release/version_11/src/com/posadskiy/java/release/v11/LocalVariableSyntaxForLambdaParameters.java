package com.posadskiy.java.release.v11;

import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class LocalVariableSyntaxForLambdaParameters {

    public static void main(String[] args) {
        var values = Stream.of(1, 2, 3)
            .map((@NotNull var value) -> value + 1)
            .peek((@NotNull var value) -> log.info(value))
            .collect(Collectors.toList());
    }

}
