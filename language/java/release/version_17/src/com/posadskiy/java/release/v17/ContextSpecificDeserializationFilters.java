package com.posadskiy.java.release.v17;

import lombok.extern.log4j.Log4j2;

import java.io.*;

/**
 * JEP 415: Context-Specific Deserialization Filters
 * <a href="https://openjdk.org/jeps/415">Docs</a>
 */
@Log4j2
public class ContextSpecificDeserializationFilters {

    public static void main(String[] args) {

        var filename = "file.ser";
        var value = new Example();

        var filter1 = ObjectInputFilter.allowFilter(cl -> cl.getPackageName().contentEquals("com.posadskiy.java.release.v17"), ObjectInputFilter.Status.REJECTED);
        ObjectInputFilter.Config.setSerialFilter(filter1);
        ObjectInputFilter.Config.setSerialFilterFactory((f1, f2) -> ObjectInputFilter.merge(f2, f1));

        serialize(value, filename);
        deserialize(filename);
    }

    public static void serialize(Object value, String filename) {
        try (var stream = new ObjectOutputStream(new FileOutputStream(filename))) {
            stream.writeObject(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void deserialize(String filename) {
        try (var stream = new ObjectInputStream(new FileInputStream(filename))) {
            ObjectInputFilter intFilter = ObjectInputFilter.rejectFilter(cl -> !cl.equals(Example.class), ObjectInputFilter.Status.UNDECIDED);
            stream.setObjectInputFilter(intFilter);

            log.info(stream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class Example implements Serializable {
    int a;
    int b;

    @Override
    public String toString() {
        return "Example{" +
            "a=" + a +
            ", b=" + b +
            '}';
    }
}
