wait() and notify() are used to manage sharing common lock between threads

notify() is preferable over notifyAll() when exactly one thread should wake up.

*notifyAll() is preferable*

- if threads have conditional logic inside

```java
synchronized (lock) {
    while (conditionA && conditionB) {
        lock.wait();
    }
}
```

- if threads have priorities

#### spurious wakeup

occurs when a thread waiting on a monitor (wait()) is woken up without an explicit call to notify() or notifyAll().
Thus, code should handle it with

```java
synchronized (lock) {
    while (!dataAvailable) { // Always use a while loop
        lock.wait(); // Wait until dataAvailable is true
    }
    // Process the available data here
}
```

#### interrupt

Interrupting a thread is effective only when the thread is actively executing or in certain other states (e.g., WAITING,
TIMED_WAITING).
