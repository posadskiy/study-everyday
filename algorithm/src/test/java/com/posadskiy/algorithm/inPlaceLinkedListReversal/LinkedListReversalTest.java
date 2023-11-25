package com.posadskiy.algorithm.inPlaceLinkedListReversal;

import com.posadskiy.algorithm.fastAndSlowIterators.LinkedListProvidesNode;
import com.posadskiy.algorithm.inPlaceLinkedListReversal.LinkedListReversal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinkedListReversalTest {

    @Test
    public void reverse() {
        LinkedListProvidesNode<Integer> list = new LinkedListProvidesNode<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        new LinkedListReversal().reverse(list);

        assertEquals(5, (int) list.get(0));
        assertEquals(4, (int) list.get(1));
        assertEquals(3, (int) list.get(2));
        assertEquals(2, (int) list.get(3));
        assertEquals(1, (int) list.get(4));
    }
}
