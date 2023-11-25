package com.posadskiy.java.core.collection.list.linkedlist;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LinkedListCustomImplementation {

    public static void main(String[] args) {
        final LinkedListCustom<Integer> numbers = new LinkedListCustom<>();

        numbers.add(0);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        log.info(numbers.toString());

        log.info(numbers.get(1));
        log.info(numbers.get(3));

        log.info(numbers.head());
        log.info(numbers.tail());

        numbers.remove();
        numbers.remove();
        log.info(numbers.toString());
    }
}

