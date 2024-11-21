package com.posadskiy.maven;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Start {

    public static void main(String[] args) {
        log.info("Starting...");
        Counter counter = new Counter();
        log.info(counter.printCount());
        log.info("Finished");
    }

}
