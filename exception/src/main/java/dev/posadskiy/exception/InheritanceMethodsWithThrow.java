package dev.posadskiy.exception;

import java.io.FileNotFoundException;
import java.io.IOException;

class InheritanceMethodsWithThrow {
    protected void execute() throws FileNotFoundException {}
}

class Children extends InheritanceMethodsWithThrow {
    @Override
    protected void execute() /*throws IOException*/ {};
}
