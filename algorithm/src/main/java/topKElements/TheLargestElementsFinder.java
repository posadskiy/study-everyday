package topKElements;

import java.util.List;

public class TheLargestElementsFinder {
    public static <T extends Comparable<T>> List<T> finder(List<T> elements, int k) {
        return ElementsFinder.finder(elements, k, (value) -> max(value.getKey(), value.getValue()));
    }
    
    private static <T extends Comparable<T>> boolean max(T first, T second) {
        return first.compareTo(second) > 0;
    }
}
