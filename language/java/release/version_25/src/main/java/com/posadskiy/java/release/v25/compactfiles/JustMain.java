/**
 * Demonstrates Compact Source Files and Instance Main Methods (JEP 512).
 * <p>
 * JEP 512 allows writing Java programs without explicit class declarations and enables
 * instance main methods. This simplifies Java code, especially for beginners and simple programs.
 * <p>
 * Key features:
 * <ul>
 *   <li>No class declaration required - compiler automatically generates a class</li>
 *   <li>Instance main methods: {@code void main()} instead of {@code public static void main(String[] args)}</li>
 *   <li>Implicit import of {@code java.base} module</li>
 * </ul>
 * <p>
 * This file demonstrates the compact syntax: a simple {@code void main()} method without
 * a class wrapper, making Java more approachable for learning and prototyping.
 * <p>
 * Reference: <a href="https://openjdk.org/jeps/512">JEP 512: Compact Source Files and Instance Main Methods</a>
 */
void main() {
    IO.print("Hello world");
}
