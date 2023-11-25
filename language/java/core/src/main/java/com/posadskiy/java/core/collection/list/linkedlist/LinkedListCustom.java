package com.posadskiy.java.core.collection.list.linkedlist;

import java.util.ArrayList;
import java.util.List;

public class LinkedListCustom<E> {
    protected NodeCustom<E> first;
    protected NodeCustom<E> last;
    private int size = 0;

    public void add(E element) {
        var node = new NodeCustom<>(element, last, null);
        var currentLast = last;
        last = node;
        if (first == null) {
            first = node;
        } else {
            currentLast.next = last;
        }
        size++;
    }

    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        NodeCustom<E> element = first;
        for (int j = 0; j < i; j++) {
            element = element.next;
        }

        return element.current;
    }

    public E remove() {
        return removeFirst();
    }

    public E head() {
        return first.current;
    }

    public E tail() {
        return last.current;
    }

    public E removeFirst() {
        final E current = first.current;
        final NodeCustom<E> firstNode = first;
        final NodeCustom<E> nextFirst = firstNode.next;

        firstNode.next = null;
        firstNode.current = null;

        first = nextFirst;
        if (first == null) {
            last = null;
        } else {
            first.previous = null;
        }

        return current;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";

        List<String> values = new ArrayList<>();
        var current = first;
        while (current != last) {
            values.add(current.current.toString());
            current = current.next;
        }

        values.add(last.current.toString());

        return "[" + String.join(", ", values) + "]";

    }
}
