package com.posadskiy.java.core.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DoubleThrow {
    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
