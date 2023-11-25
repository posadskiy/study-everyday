package com.posadskiy.algorithm.depthFirstSearch;

import com.posadskiy.algorithm.depthFirstSearch.TreeNodeLeftRight;

public class TreeLeftRightCreator {
    
    public static TreeNodeLeftRight<Integer> getTree() {
        final TreeNodeLeftRight<Integer> tree = new TreeNodeLeftRight<>(5);

        var leftFirstLevel = tree.addLeft(10);
        var rightFirstLevel = tree.addRight(20);
        
        var leftSecondLevelOne = leftFirstLevel.addLeft(4);
        var leftSecondLevelTwo = rightFirstLevel.addLeft(15);
        var rightSecondLevelOne = leftFirstLevel.addRight(16);
        var rightSecondLevelTwo = rightFirstLevel.addRight(26);

        return tree;
    }
}
