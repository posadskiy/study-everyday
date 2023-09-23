package inPlaceLinkedListReversal;

import core.collection.list.linkedlist.NodeCustom;
import fastAndSlowIterators.LinkedListProvidesNode;

public class LinkedListReversal {

    public void reverse(LinkedListProvidesNode<Integer> list) {
        NodeCustom<Integer> first = list.getFirstNode();
        NodeCustom<Integer> previous = null;
        NodeCustom<Integer> current = list.getFirstNode();
        NodeCustom<Integer> next;

        while (current != list.getLastNode()) {
            next = current.next;
            current.next = previous;
            previous = current;
            current = next;
            previous.previous = current;
        }
        
        current.previous = null;
        current.next = previous;

        list.setLastNode(first);
        list.setFirstNode(current);
    }
}
