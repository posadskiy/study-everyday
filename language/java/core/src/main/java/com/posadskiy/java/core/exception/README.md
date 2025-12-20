# Exceptions

Exceptions are used when program execution has hit a dead end — when it’s impossible to continue running the application
normally.

#### When it’s useful

One important use of exceptions is the ability to unwind the stack with a single statement. Catching exceptions is used
to allow stack cleanup up to a specific point. Imagine the stack levels below:

1 -\
2 --\
3 --- `catch (MyException e) { ... }`\
4 ----\
5 ----- `throw new MyException();`\

In this example, throwing an exception on level 5 lets you return immediately to level 3, skipping intermediate frames,
and continue program execution.

#### Classification

Exceptions are divided into checked (`checked`) and unchecked (`unchecked`). The former are usually caused by runtime
conditions the program must handle explicitly, while the latter are usually due to programming errors or system issues.

#### Catch unchecked without declaring

In a `catch` handler you can catch an unchecked exception even if the code in `try` does not explicitly declare it can be
thrown. But you can’t ask the program to catch checked exceptions that cannot be thrown from the `try` block — the
compiler won’t allow it.

```java
public class NotThrowButCatch {
    public static void main(String[] args) {
        try {
            String s = "This code don't generate any exceptions";
        } catch (NullPointerException | IllegalArgumentException/*FileNotFoundException*/ e) { // FileNotFoundException - checked
            e.printStackTrace();
        }
    }
}
```

#### You can’t catch the same thrown exception twice

An `Exception` thrown from a `catch` block is not caught by another `catch` later in the same `try` statement, even if it
matches that handler’s type.

```java
public class DoubleThrow {
    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            throw e;
        } catch (Exception e) { // NPE will not be caught here, although it is a subclass of Exception
            log.info(e.getMessage());
        }
    }
}
```

#### Throwing `Throwable` that wraps an `Exception`

If you throw a parent type that actually contains a child instance, you can catch the child type — but you must also
have a handler for the parent type, or (as below) the method signature must declare it can throw the parent. Other
variants won’t compile.

```java
public class ThrowThrowableCatchException {
    public static void main(String[] args) throws Throwable {
        try {
            Throwable t = new Exception();
            throw t;
        } catch (Exception e) {
            System.err.println("Exception");
        }
    }
}
```

#### Overriding methods that throw exceptions

When overriding, the list of exceptions a method can throw can stay the same or be narrowed, but cannot be widened. If it
could be widened, callers expecting the parent type might receive an unexpected “more serious” exception. In the example
below the child declares it can throw `IOException`, which is a parent of `FileNotFoundException`.

```java
class InheritanceMethodsWithThrow {
    protected void execute() throws FileNotFoundException {}
}

class Children extends InheritanceMethodsWithThrow {
    @Override
    protected void execute() throws IOException {}
}
```

#### Exceptions are effectively final in multi-catch

An exception caught in a `catch` block can be reassigned, but only to an instance of the same class or a subclass.

```java
public class DoNotChangeException {
    public static void main(String[] args) {
        try {
            throw new IOException("In-try exception");
        } catch (IOException e) {
            e = new IOException("In-catch exception");
            e = new EOFException();
            // e = new Exception("In-catch exception"); Not allowed.
            e.printStackTrace();
        }
    }
}
```

#### `System.err`

`System.out` is buffered, while `System.err` is not. Therefore, in an example like:

```java
public class Start {
    public static void main(String[] args) {
        log.info("Message");
        throw new Error("Error");
    }
}
```

you might see `Message` printed before `Error`, or vice versa. If you print to `System.err` instead, you will always see
`Message` first and then `Error`.

#### `throw null`

Java allows throwing a null exception: `throw null`. In that case Java checks the argument and throws a
`NullPointerException`.


