package com.posadskiy.java.release.v11;

import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;

@Log4j2
public class StringNewApi {

    public static void main(String[] args) {
        log.info("".isBlank());
        log.info("\ntest\ntest".lines().collect(Collectors.toList()));
        log.info("a".repeat(10));
        log.info("   this  is    an    string      example   ".strip());
    }

}
