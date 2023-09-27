package core.collection.map.treemap;

import java.util.List;

public class TreeMapNode<K, V> {
    private K key;
    private V value;
    private List<TreeMapNode<K, V>> children;
}
