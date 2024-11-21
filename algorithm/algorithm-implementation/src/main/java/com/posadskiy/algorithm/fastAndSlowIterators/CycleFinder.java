package com.posadskiy.algorithm.fastAndSlowIterators;

import com.posadskiy.java.core.collection.list.linkedlist.NodeCustom;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import static com.posadskiy.algorithm.fastAndSlowIterators.CycledListCreator.create;

@Log4j2
public class CycleFinder {

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

