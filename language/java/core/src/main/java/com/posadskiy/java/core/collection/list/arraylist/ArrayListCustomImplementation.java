package com.posadskiy.java.core.collection.list.arraylist;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ArrayListCustomImplementation {

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

