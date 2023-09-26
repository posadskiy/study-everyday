### Garbage collector (GC)

Two phases of work:

- Mark - add label to objects without any link on it or weak link
- Sweep - remove "marked" objects

Pros:
Automatic garbaging, almost no memory leak, no Dangling (Wild) pointer

Dangling (Wild) pointer - pointer, reffers to object, continue to point to empty memory block after removing related
object.

Cons:
CPU, slowdowns, application pauses, not so efficient as manual, no control.

#### Serial GC

Single-thread GC makes application pauses.

#### Parallel GC

Parallel GC makes Mark phase in parallel, but Sweep with freeze all threads.
Default for from Java 5 to 8.
Maximum pause could be controlled with MaxGCPauseMillis parameter.

#### Garbage First G1 GC

Parallel GC, default for Java from 9 to 21.
GC split heap into equal-size regions. After performing Mark phase, it determine which regions are mostly emtpy and runs
Sweep there. As a result - more free space easely.

#### Z GC (low latency)

Z takes all from parallel GC, but doesn't stop application more than 10ms. It uses load barrers with colored pointers to
perform concurrent operations. Color pointer - additional metadata for link to object to highlight its state.
From Java 15.

#### Shenandoah GC

Parallel GC, reduces pause times by permorming more GC work concurrently with working application. It's regionalized
collector, it maintains the heap as the collection of regions.
Java 8, 11, 17.

![Alt text](./resources/shenandoah-gc-cycle.svg)
