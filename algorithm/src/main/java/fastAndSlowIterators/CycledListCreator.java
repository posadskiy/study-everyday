package fastAndSlowIterators;

import java.util.stream.IntStream;

public class CycledListCreator {
    public static LinkedListProvidesNode<Integer> create() {
        final LinkedListProvidesNode<Integer> list = new LinkedListProvidesNode<Integer>();
        IntStream.range(0, 10)
            .boxed()
            .forEach(list::add);

        var currentNode = list.getFirstNode();
        var lastNode = list.getLastNode();

        currentNode = currentNode.next.next.next.next;
        lastNode.next = currentNode;

        return list;
    }
}
