package com.posadskiy.algorithm.breadthFirstSearch;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeWithAccess<T> {

    @Getter
    private final T value;

    @Getter
    private final List<TreeNodeWithAccess<T>> children;

    public TreeNodeWithAccess(T value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public TreeNodeWithAccess<T> of(T value) {
        return new TreeNodeWithAccess<>(value);
    }

    public TreeNodeWithAccess<T> add(T value) {
        final TreeNodeWithAccess<T> child = new TreeNodeWithAccess<>(value);
        children.add(child);
        return child;
    }
}
