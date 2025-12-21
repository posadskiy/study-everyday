package com.posadskiy.java.release.v25.earlysupercall;

import lombok.extern.log4j.Log4j2;

/**
 * Demonstrates Flexible Constructor Bodies (JEP 513).
 * <p>
 * JEP 513 allows statements to appear before explicit constructor invocations
 * ({@code super(...)} or {@code this(...)}), providing greater flexibility and safety
 * during object construction.
 * <p>
 * Key benefits:
 * <ul>
 *   <li>Validate arguments before calling superclass constructor</li>
 *   <li>Initialize fields before superclass constructor runs</li>
 *   <li>More natural code flow and better error handling</li>
 * </ul>
 * <p>
 * Restrictions: Pre-invocation statements cannot reference {@code this} (object not fully constructed).
 * <p>
 * Reference: <a href="https://openjdk.org/jeps/513">JEP 513: Flexible Constructor Bodies</a>
 */
@Log4j2
public class EarlySuperCallInConstructor {

    // Base class
    static class Person {
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
            log.info("Person created: {} (age: {})", name, age);
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }
    }

    /**
     * Example 1: Validation before super() call (JEP 513)
     * <p>
     * With JEP 513, we can validate arguments BEFORE calling super(),
     * ensuring we fail fast if validation fails.
     */
    static class Employee extends Person {
        private final String department;

        Employee(String name, int age, String department) {
            // JEP 513: Can validate BEFORE super() call
            if (age < 18 || age > 67) {
                throw new IllegalArgumentException(
                    "Invalid age for employee: " + age + ". Must be between 18 and 67.");
            }
            if (department == null || department.isBlank()) {
                throw new IllegalArgumentException("Department cannot be null or blank");
            }
            // Now call super() after validation
            super(name, age);
            this.department = department;
            log.info("Employee created: {} in department {}", name, department);
        }

        String getDepartment() {
            return department;
        }
    }

    /**
     * Example 2: Field initialization before super() call
     * <p>
     * Can initialize fields before calling super(), ensuring they're set
     * before the superclass constructor potentially accesses them.
     */
    static class Manager extends Person {
        private final int teamSize;
        private final String level;

        Manager(String name, int age, int teamSize) {
            // JEP 513: Can compute/initialize fields before super()
            this.teamSize = teamSize;
            this.level = teamSize > 10 ? "Senior" : "Junior";

            // Validate using initialized fields
            if (teamSize < 0) {
                throw new IllegalArgumentException("Team size cannot be negative");
            }

            super(name, age);
            log.info("Manager created: {} (level: {}, team size: {})", name, level, teamSize);
        }

        int getTeamSize() {
            return teamSize;
        }

        String getLevel() {
            return level;
        }
    }

    /**
     * Example 3: Using static methods before super() call
     * <p>
     * Can call static methods to prepare arguments before passing to super().
     */
    static class ValidatedPerson extends Person {
        ValidatedPerson(String name, int age) {
            // JEP 513: Can call static methods before super()
            String normalizedName = normalizeName(name);
            int validatedAge = validateAge(age);

            super(normalizedName, validatedAge);
        }

        private static String normalizeName(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Name cannot be null or blank");
            }
            return name.trim();
        }

        private static int validateAge(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
            return age;
        }
    }

    static void main() {
        log.info("=== Demonstrating JEP 513: Flexible Constructor Bodies ===\n");

        // Example 1: Valid employee
        try {
            Employee emp1 = new Employee("Alice", 30, "Engineering");
            log.info("Successfully created: {} (age: {}, dept: {})\n",
                emp1.getName(), emp1.getAge(), emp1.getDepartment());
        } catch (IllegalArgumentException e) {
            log.error("Failed to create employee: {}", e.getMessage());
        }

        // Example 1: Invalid age (validation happens BEFORE super() call)
        try {
            Employee emp2 = new Employee("Bob", 15, "Sales");
            log.info("Created: {}", emp2.getName());
        } catch (IllegalArgumentException e) {
            log.info("Validation caught invalid age BEFORE super() call: {}\n", e.getMessage());
        }

        // Example 2: Manager with field initialization
        try {
            Manager mgr = new Manager("Charlie", 35, 15);
            log.info("Successfully created: {} (level: {}, team: {})\n",
                mgr.getName(), mgr.getLevel(), mgr.getTeamSize());
        } catch (IllegalArgumentException e) {
            log.error("Failed: {}", e.getMessage());
        }

        // Example 3: Validated person
        try {
            ValidatedPerson person = new ValidatedPerson("  David  ", 25);
            log.info("Successfully created validated person: {} (age: {})\n",
                person.getName(), person.getAge());
        } catch (IllegalArgumentException e) {
            log.error("Validation failed: {}", e.getMessage());
        }
    }
}
