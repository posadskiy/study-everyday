package com.posadskiy.algorithm.breadthFirstSearch;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class BreadthFirstSearcherTest {

    @Test
    public void search() {
        final TreeNodeWithAccess<Integer> tree = TreeCreator.getTree();
        BreadthFirstSearcher<Integer> searcher = new BreadthFirstSearcher<>();
        var expected = 70;

        final Optional<TreeNodeWithAccess<Integer>> search = searcher.search(tree, 70);
        
        assertEquals(expected, (int) search.orElseThrow(RuntimeException::new).getValue());
    }
}
