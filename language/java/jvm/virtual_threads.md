A virtual thread is an instance of java.lang.Thread that is not tied to a particular OS thread. A platform thread, by
contrast, is an instance of java.lang.Thread implemented in the traditional way, as a thin wrapper around an OS thread.

Application code in the thread-per-request style can run in a virtual thread for the entire duration of a request, but
the virtual thread consumes an OS thread only while it performs calculations on the CPU. The result is the same
scalability as the asynchronous style, except it is achieved transparently: When code running in a virtual thread calls
a blocking I/O operation in the java.* API, the runtime performs a non-blocking OS call and automatically suspends the
virtual thread until it can be resumed later. To Java developers, virtual threads are simply threads that are cheap to
create and almost infinitely plentiful. Hardware utilization is close to optimal, allowing a high level of concurrency
and, as a result, high throughput, while the application remains harmonious with the multithreaded design of the Java
Platform and its tooling.

Virtual threads can significantly improve application throughput when

* The number of concurrent tasks is high (more than a few thousand), and
* The workload is not CPU-bound, since having many more threads than processor cores cannot improve throughput in that case.

From the perspective of Java code, a running virtual thread is logically independent of its current carrier:

* The identity of the carrier is unavailable to the virtual thread. The value returned by Thread.currentThread() is always the virtual thread itself.
* The stack traces of the carrier and the virtual thread are separate. An exception thrown in the virtual thread will not include the carrier's stack frames. Thread dumps will not show the carrier's stack frames in the virtual thread's stack, and vice-versa.
* Thread-local variables of the carrier are unavailable to the virtual thread, and vice-versa

There are two scenarios in which a virtual thread cannot be unmounted during blocking operations because it is pinned to its carrier:

* When it executes code inside a synchronized block or method, or
* When it executes a native method or a foreign function.
