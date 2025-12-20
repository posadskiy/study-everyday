# JEP 491: Synchronize Virtual Threads without Pinning

JEP 491 allows virtual threads to release their platform threads even when blocked within `synchronized` constructs. Previously, virtual threads would become "pinned" to platform threads when entering synchronized blocks, preventing unmounting and limiting scalability.

This enhancement eliminates most pinning scenarios, enabling:
- Higher concurrency with virtual threads
- Better scalability for applications using `synchronized` methods/statements
- No need to refactor existing code to avoid `synchronized` for virtual thread compatibility

