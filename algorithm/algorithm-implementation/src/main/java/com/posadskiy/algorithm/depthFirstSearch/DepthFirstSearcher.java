package com.posadskiy.algorithm.depthFirstSearch;

import java.util.Optional;
import java.util.Stack;

public class DepthFirstSearcher {
    public static <T> Optional<TreeNodeLeftRight<T>> search(TreeNodeLeftRight<T> node, T value) {
        Stack<TreeNodeLeftRight<T>> stack = new Stack<>();
        var current = node;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            var top = stack.pop();
            if (value.equals(top.getValue())) {
                return Optional.of(top);
            }

            current = top.getRight();
        }

        return Optional.empty();
    }
}
