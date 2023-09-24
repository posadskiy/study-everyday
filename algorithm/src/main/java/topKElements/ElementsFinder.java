package topKElements;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Predicate;

public class ElementsFinder {
    public static <T extends Comparable<T>> List<T> finder(List<T> elements, int k, Predicate<Map.Entry<T, T>> predicate) {
        return finder(elements, k, predicate, Comparator.naturalOrder());
    }

    public static <T extends Comparable<T>> List<T> finder(List<T> elements, int k, Predicate<Map.Entry<T, T>> predicate, Comparator<T> comparator) {
        final PriorityQueue<T> queue = new PriorityQueue<>(comparator);

        for (T element : elements) {
            if (queue.size() < k) {
                queue.add(element);
                continue;
            }

            final T second = queue.poll();
            final boolean test = predicate.test(Map.entry(element, second));
            queue.offer(test ? element : second);
        }

        return queue.stream().toList();
    }
}
