package com.posadskiy.java.core.collection.list.arraylist;

import java.util.Arrays;

public class ArrayListCustom<E> {
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
