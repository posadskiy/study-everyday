package fastandslowiterator;

import core.collection.list.linkedlist.LinkedListCustom;
import core.collection.list.linkedlist.NodeCustom;

class LinkedListProvidesNode<E> extends LinkedListCustom<E> {
    
    public NodeCustom<E> getFirstNode() {
        return first;
    }
    
    public NodeCustom<E> getLastNode() {
        return last;
    }

}
