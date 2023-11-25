package com.posadskiy.java.core;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Initialization {
    private String whereInit = "Field init";
    private int value;

    {
        log.info("Block initialization");
        log.info(whereInit);
        this.whereInit = "Block init";
        log.info(whereInit);
    }

    public static void main(String[] args) {
        Initialization init = new Initialization();
    }

    public void Initialization() {
        log.info("Constructor initialization");
        log.info(whereInit);
        this.whereInit = "Constructor init";
        log.info(whereInit);
    }

    public void checkValue() {
        log.info(value);

        int localValue;
        // log.info(localValue); NOT COMPILE
    }
}
