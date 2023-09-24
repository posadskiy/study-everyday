package topKElements;

import java.util.List;
import java.util.PriorityQueue;

public class TheLargestElementsFinder {
    public static <T extends Comparable<T>> List<T> finder(List<T> elements, int k) {
        final PriorityQueue<T> queue = new PriorityQueue<>();

        for (T element : elements) {
            if (queue.size() < k) {
                queue.add(element);
                continue;
            }
            
            queue.offer(max(element, queue.poll()));
        }
        
        return queue.stream().toList();
    }
    
    private static <T extends Comparable<T>> T max(T first, T second) {
        return first.compareTo(second) > 0 ? first : second;
    }
}
