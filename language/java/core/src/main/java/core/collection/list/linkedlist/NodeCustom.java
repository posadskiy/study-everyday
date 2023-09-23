package core.collection.list.linkedlist;

public class NodeCustom<E> {
    public NodeCustom<E> previous;
    public NodeCustom<E> next;
    public E current;

    public NodeCustom(E current, NodeCustom<E> previous, NodeCustom<E> next) {
        this.previous = previous;
        this.current = current;
        this.next = next;
    }
}
