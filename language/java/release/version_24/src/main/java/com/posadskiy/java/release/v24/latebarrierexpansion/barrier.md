# JEP 475: Late Barrier Expansion for G1

JEP 475 simplifies the G1 garbage collector's barriers by moving their expansion to a later stage in the C2 JIT compiler's pipeline. Previously, barriers were expanded earlier in the compilation process.

This optimization provides:
- Reduced C2 compilation time
- Improved code quality and maintainability
- Better understanding of G1 barriers for developers
- Correct ordering of memory accesses, safepoints, and barriers

