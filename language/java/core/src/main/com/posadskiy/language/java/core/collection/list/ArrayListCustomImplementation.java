package com.posadskiy.language.java.core.collection.list;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class ArrayListCustomImplementation {
    private final static Logger log = LogManager.getLogger(ArrayListCustomImplementation.class);

    public static void main(String[] args) {
        final ArrayListCustom<Integer> numbers = new ArrayListCustom<>();

        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        log.info(numbers.toString());

        log.info(numbers.get(3));
        log.info(numbers.get(4));

        log.info(numbers.remove());
        log.info(numbers.remove());
        log.info(numbers.toString());
    }
}

class ArrayListCustom<E> {
    private final static int DEFAULT_GROW_NUMBER = 10;
    Object[] elements;

    int size;

    public void add(E element) {
        if (elements == null || size == elements.length) {
            grow(size + 1);
        }

        elements[size] = element;
        size++;
    }

    public E remove() {
        @SuppressWarnings("unchecked") final E element = (E) elements[size - 1];

        elements[size - 1] = null;
        size--;

        return element;
    }

    public int size() {
        return size;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Requested element is out of List");
        }

        @SuppressWarnings("unchecked") final E element = (E) elements[index];
        return element;
    }

    private void grow(int size) {
        if (elements == null) {
            elements = new Object[DEFAULT_GROW_NUMBER];
        }
        elements = Arrays.copyOf(elements, size + DEFAULT_GROW_NUMBER);
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        return "["
            + String.join(", ",
            Arrays.stream(elements)
                .limit(size)
                .map(Object::toString)
                .toList())
            + "]";

    }
}
