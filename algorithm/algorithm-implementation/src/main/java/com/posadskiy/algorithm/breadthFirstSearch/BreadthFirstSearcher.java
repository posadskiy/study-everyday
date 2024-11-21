package com.posadskiy.algorithm.breadthFirstSearch;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class BreadthFirstSearcher<T> {
    public Optional<TreeNodeWithAccess<T>> search(TreeNodeWithAccess<T> tree, T value) {
        Queue<TreeNodeWithAccess<T>> queue = new ArrayDeque<>();
        queue.add(tree);

        while (!queue.isEmpty()) {
            final TreeNodeWithAccess<T> node = queue.remove();

            if (value.equals(node.getValue())) {
                return Optional.of(node);
            } else {
                queue.addAll(node.getChildren());
            }
        }

        return Optional.empty();
    }
}
