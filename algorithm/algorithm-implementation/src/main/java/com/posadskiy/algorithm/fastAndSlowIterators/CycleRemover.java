package com.posadskiy.algorithm.fastAndSlowIterators;

import com.posadskiy.java.core.collection.list.linkedlist.NodeCustom;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
public class CycleRemover {

    public static void main(String[] args) {
        var list = CycledListCreator.create();

        log.info(CycleFinder.findCycle(list).isPresent());
        removeCycle(list);
        log.info(CycleFinder.findCycle(list).isPresent());
    }


    public static void removeCycle(LinkedListProvidesNode<Integer> list) {
        final Optional<NodeCustom<Integer>> cycle = CycleFinder.findCycle(list);
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
