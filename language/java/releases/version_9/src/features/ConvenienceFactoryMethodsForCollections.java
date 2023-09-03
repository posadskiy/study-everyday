package features;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JEP 269: Convenience Factory Methods for Collections
 * <p>
 * <a href="https://openjdk.org/jeps/269">Docs</a>
 */
public class ConvenienceFactoryMethodsForCollections {
    private final static Logger log = System.getLogger("default");

    public static void main(String[] args) {
        final Set<Integer> integerSet = Set.of(1, 2, 3);
        final Map<Integer, Integer> integerMap = Map.of(0, 1, 1, 2, 2, 3);
        final List<Integer> integerList = List.of(1, 2, 3);
        
        log.log(Level.INFO, integerSet);
        log.log(Level.INFO, integerMap);
        log.log(Level.INFO, integerList);
    }

}
