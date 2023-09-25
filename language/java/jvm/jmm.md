### Java Memory Model (JMM)

JMM is divided on Stack memory and Heap memory

#### Stack memory

Stack memory is used for static memory allocation and execution of the thread. Contains primitives method-specific
values and references to objects in Heap. Works as usual stack - LIFO, grow and shrink when method calls and returned.

Pros:
Fast access, thread-safe (new thread - new stack), automatic memory allocation and deallocation

#### Heap space

Heap space is used for dynamic memory allocation of Java object and JRE classes in runtime.

- Young generation - new objects, GC runs when full
- Old generation - long living objects storage, moves here when live more than threshold time.
- Permament generation - JVM metadata

Cons:
Dealocation via GC, no thread-safe, slower access.
