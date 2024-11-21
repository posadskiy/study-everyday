package com.posadskiy.algorithm.breadthFirstSearch;

import com.posadskiy.algorithm.breadthFirstSearch.TreeNodeWithAccess;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TreeCreator {
    
    public static TreeNodeWithAccess<Integer> getTree() {
        final TreeNodeWithAccess<Integer> tree = new TreeNodeWithAccess<>(5);

        IntStream.range(0, 10).boxed().forEach(tree::add);

        AtomicInteger i = new AtomicInteger(1);
        tree.getChildren()
            .forEach(node ->
                IntStream.range(10 * i.get(), 10 * (i.getAndIncrement() + 1))
                    .boxed()
                    .forEach(node::add)
            );

        return tree;
    }
}
