package core.collection.list.linkedlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LinkedListCustomImplementation {
    private final static Logger log = LogManager.getLogger(LinkedListCustomImplementation.class);

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

