package com.posadskiy.java.release.v25.moduleimport;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates Module Import Declarations (JEP 511).
 * <p>
 * Module Import Declarations allow importing an entire module with a single import statement,
 * replacing multiple individual package imports. This feature helps reduce "import hell" and
 * makes code more concise.
 * <p>
 * Example: Instead of importing individual classes like {@code java.util.List}, {@code java.util.Map},
 * you can use: {@code import module java.base.*;} to import all packages from the java.base module.
 * <p>
 * This feature was introduced as a preview in Java 23 and became a production feature in Java 25.
 * It works only with named modules (not unnamed modules or pre-Java 9 libraries).
 * <p>
 * References:
 * <ul>
 *   <li><a href="https://openjdk.org/jeps/511">JEP 511: Module Import Declarations</a></li>
 *   <li><a href="https://blog.jetbrains.com/idea/2025/07/module-import-declarations-no-more-import-hell/">JetBrains Blog: Module Import Declarations</a></li>
 * </ul>
 *
 * @see <a href="https://openjdk.org/jeps/511">JEP 511</a>
 */
@Log4j2
public class TryModuleImport {
    public void justMethod() {
        var list = List.of(1);
        var map = Map.of("key", "value");
        var hashMap = HashMap.newHashMap(1);

        // Some meaningless code just to make variables used
        log.info("Sizes: {} {} {}", list.size(), map.size(), hashMap.size());
    }

    static void main() {
        var tryModuleImport = new TryModuleImport();
        tryModuleImport.justMethod();
    }
}
