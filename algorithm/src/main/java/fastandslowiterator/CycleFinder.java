package fastandslowiterator;

import core.collection.list.linkedlist.NodeCustom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static fastandslowiterator.CycledListCreator.create;

public class CycleFinder {
    private final static Logger log = LogManager.getLogger(CycleFinder.class);

    public static void main(String[] args) {
        var list = create();

        log.info(findCycle(list).isPresent());
    }

    public static Optional<NodeCustom<Integer>> findCycle(LinkedListProvidesNode<Integer> list) {
        var fast = list.getFirstNode();
        var slow = list.getFirstNode();

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return Optional.ofNullable(slow);
            }
        }

        return Optional.empty();
    }
}

