package fastandslowiterator;

import core.collection.list.linkedlist.NodeCustom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static fastandslowiterator.CycleFinder.findCycle;
import static fastandslowiterator.CycledListCreator.create;


public class CycleRemover {
    private final static Logger log = LogManager.getLogger(CycleRemover.class);

    public static void main(String[] args) {
        var list = create();

        log.info(findCycle(list).isPresent());
        removeCycle(list);
        log.info(findCycle(list).isPresent());
    }


    public static void removeCycle(LinkedListProvidesNode<Integer> list) {
        final Optional<NodeCustom<Integer>> cycle = findCycle(list);
        if (cycle.isEmpty()) return;

        var first = list.getFirstNode();
        var second = cycle.get();

        while (first != second) {
            first = first.next;
            second = second.next;
        }

        while (second.next != first) {
            second = second.next;
        }

        second.next = null;
    }
}
