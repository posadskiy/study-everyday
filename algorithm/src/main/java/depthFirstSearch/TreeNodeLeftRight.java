package depthFirstSearch;

import lombok.Getter;

public class TreeNodeLeftRight<T> {

    @Getter
    private T value;
    
    @Getter
    private TreeNodeLeftRight<T> left;
    
    @Getter
    private TreeNodeLeftRight<T> right;
    
    public TreeNodeLeftRight(T value) {
        this.value = value;
    }
    
    public TreeNodeLeftRight<T> addLeft(T value) {
        final TreeNodeLeftRight<T> leftNode = new TreeNodeLeftRight<>(value);
        this.left = leftNode;
        return leftNode;
    }
    
    public TreeNodeLeftRight<T> addRight(T value) {
        final TreeNodeLeftRight<T> rightNode = new TreeNodeLeftRight<>(value);
        this.right = rightNode;
        return rightNode;
    }
    
    
}
