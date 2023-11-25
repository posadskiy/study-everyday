package com.posadskiy.java.release.v16;

import lombok.extern.log4j.Log4j2;

/**
 * JEP 395: Records
 * <p>
 * Rules for record classes
 * <p>
 * There are numerous restrictions on the declaration of a record class in comparison to a normal class:
 * <p>
 * A record class declaration does not have an extends clause. The superclass of a record class is always java.lang.Record, similar to how the superclass of an enum class is always java.lang.Enum. Even though a normal class can explicitly extend its implicit superclass Object, a record cannot explicitly extend any class, even its implicit superclass Record.
 * <p>
 * A record class is implicitly final, and cannot be abstract. These restrictions emphasize that the API of a record class is defined solely by its state description, and cannot be enhanced later by another class.
 * <p>
 * The fields derived from the record components are final. This restriction embodies an immutable by default policy that is widely applicable for data-carrier classes.
 * <p>
 * A record class cannot explicitly declare instance fields, and cannot contain instance initializers. These restrictions ensure that the record header alone defines the state of a record value.
 * <p>
 * Any explicit declarations of a member that would otherwise be automatically derived must match the type of the automatically derived member exactly, disregarding any annotations on the explicit declaration. Any explicit implementation of accessors or the equals or hashCode methods should be careful to preserve the semantic invariants of the record class.
 * <p>
 * A record class cannot declare native methods. If a record class could declare a native method then the behavior of the record class would by definition depend on external state rather than the record class's explicit state. No class with native methods is likely to be a good candidate for migration to a record.
 * <p>
 * Beyond the restrictions above, a record class behaves like a normal class:
 * <p>
 * Instances of record classes are created using a new expression.
 * <p>
 * A record class can be declared top level or nested, and can be generic.
 * <p>
 * A record class can declare static methods, fields, and initializers.
 * <p>
 * A record class can declare instance methods.
 * <p>
 * A record class can implement interfaces. A record class cannot specify a superclass since that would mean inherited state, beyond the state described in the header. A record class can, however, freely specify superinterfaces and declare instance methods to implement them. Just as for classes, an interface can usefully characterize the behavior of many records. The behavior may be domain-independent (e.g., Comparable) or domain-specific, in which case records can be part of a sealed hierarchy which captures the domain (see below).
 * <p>
 * A record class can declare nested types, including nested record classes. If a record class is itself nested, then it is implicitly static; this avoids an immediately enclosing instance which would silently add state to the record class.
 * <p>
 * A record class, and the components in its header, may be decorated with annotations. Any annotations on the record components are propagated to the automatically derived fields, methods, and constructor parameters, according to the set of applicable targets for the annotation. Type annotations on the types of record components are also propagated to the corresponding type uses in the automatically derived members.
 * <p>
 * Instances of record classes can be serialized and deserialized. However, the process cannot be customized by providing writeObject, readObject, readObjectNoData, writeExternal, or readExternal methods. The components of a record class govern serialization, while the canonical constructor of a record class governs deserialization.
 * <p>
 * <a href="https://openjdk.org/jeps/395">Docs</a>
 */
@Log4j2
public class Records {

    public static void main(String[] args) {
        final Point point = new Point(2, 3);

        log.info(point.x());
        log.info(point.y());
        log.info(point.toString());
        log.info(point.hashCode());
        log.info(point.equals(new Point(2, 3)));

        try {
            new ValidatedPoint(-1, 0);
        } catch (IllegalArgumentException e) {
            log.info("Exception: " + e.getLocalizedMessage());
        }

        final PointOverride pointOverride = new PointOverride(-10, -5);
        log.info(pointOverride.x());
        log.info(pointOverride.y());

        record RecordInline(int x, int y) {
        }

        final RecordInline recordInline = new RecordInline(point.x(), point.y());
        log.info(recordInline.x());
        log.info(recordInline.y());

    }
}

record Point(int x, int y) {
}

record ValidatedPoint(int x, int y) {
    ValidatedPoint {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates should be positive");
        }
    }
}

record PointOverride(int x, int y) {
    public int x() {
        return Math.max(this.x, 0);
    }

    public int y() {
        return Math.max(this.y, 0);
    }
}
