package depthFirstSearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class DepthFirstSearcherTest {

    @Test
    public void search() {
        final TreeNodeLeftRight<Integer> tree = TreeLeftRightCreator.getTree();
        var expected = 26;

        var result = DepthFirstSearcher.search(tree, expected);
        
        assertEquals(expected, (int) result.orElseThrow(RuntimeException::new).getValue());
    }
}
