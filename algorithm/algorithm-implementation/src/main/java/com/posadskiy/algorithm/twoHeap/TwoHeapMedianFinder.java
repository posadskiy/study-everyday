package com.posadskiy.algorithm.twoHeap;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class TwoHeapMedianFinder {
    Queue<Integer> small = new PriorityQueue<>();
    Queue<Integer> large = new PriorityQueue<>(Collections.reverseOrder());
    private boolean even = true;

    public void add(int n) {
        if (even) {
            small.offer(n);
            large.offer(small.poll());
        } else {
            large.offer(n);
            small.offer(large.poll());
        }
        even = !even;
    }

    public double findMedian() {
        if (even) {
            return (small.peek() + large.peek()) / 2.0;
        }

        return large.peek();
    }
}
