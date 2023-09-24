package fastAndSlowIterators;

import core.collection.list.linkedlist.LinkedListCustom;
import core.collection.list.linkedlist.NodeCustom;

public class LinkedListProvidesNode<E> extends LinkedListCustom<E> {

    public NodeCustom<E> getFirstNode() {
        return first;
    }

    public void setFirstNode(NodeCustom<E> first) {
        this.first = first;
    }

    public NodeCustom<E> getLastNode() {
        return last;
    }

    public void setLastNode(NodeCustom<E> last) {
        this.last = last;
    }

}
