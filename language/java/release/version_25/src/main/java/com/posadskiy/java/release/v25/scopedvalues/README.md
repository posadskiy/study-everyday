# Scoped Values (JEP 506)

**JEP 506**: [Scoped Values](https://openjdk.org/jeps/506) - Introduced in Java 25

## Overview

Scoped Values provide a mechanism for safely and efficiently sharing immutable data between methods within the same thread and with child threads, without the need to pass data explicitly as method parameters.

## Key Features

- **Immutable Data Sharing**: Values are bound once per scope and cannot be mutated
- **Automatic Cleanup**: Values are automatically unbound when the scope exits
- **Efficient Propagation**: Designed to work efficiently with virtual threads and structured concurrency
- **One-Way Data Flow**: Clear, predictable data flow from boundary to callees

## Basic Usage

```java
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();

ScopedValue.where(USER_ID, "user123").run(() -> {
    processUser(); // Can access USER_ID.get() without passing it as parameter
});
```

## Advantages Over ThreadLocal

1. **Unconstrained Mutability**: ThreadLocal allows `get()`/`set()` from anywhere, leading to unclear data flow. ScopedValue provides one-way, immutable binding.

2. **Unbounded Lifetime**: ThreadLocal values persist for thread lifetime unless `remove()` is called, causing leaks. ScopedValue has bounded lifetime with automatic cleanup.

3. **Expensive Inheritance**: InheritableThreadLocal requires copying all values to child threads. ScopedValue efficiently shares values with child tasks in structured concurrency.

## See Also

- [JEP 506: Scoped Values](https://openjdk.org/jeps/506)
- Example code: `TryScopedValues.java`

