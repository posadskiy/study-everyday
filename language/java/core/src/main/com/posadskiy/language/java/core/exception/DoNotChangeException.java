package com.posadskiy.language.java.core.exception;

import java.io.EOFException;
import java.io.IOException;

public class DoNotChangeException {
    public static void main(String[] args) {
        try {
            throw new IOException("In-try exception");
        } catch (IOException e) {
            e = new IOException();
            e = new EOFException();
            //e = new Exception("In-catch exception");
            e.printStackTrace();
        }
    }
}
