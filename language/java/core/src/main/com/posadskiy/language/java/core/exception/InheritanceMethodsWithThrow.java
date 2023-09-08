package com.posadskiy.language.java.core.exception;

import java.io.FileNotFoundException;

class InheritanceMethodsWithThrow {
    protected void execute() throws FileNotFoundException {}
}

class Children extends InheritanceMethodsWithThrow {
    @Override
    protected void execute() /*throws IOException*/ {};
}
