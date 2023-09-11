import java.util.Set;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;


/**
 * JEP 356: Enhanced Pseudo-Random Number Generators
 * <a href="https://openjdk.org/jeps/356">Docs</a>
 */
public class RandomGenerators {
    private final static System.Logger log = System.getLogger("default");

    public static void main(String[] args) {
        // Before 
        final SplittableRandom splittableRandom = new SplittableRandom();
        log.log(System.Logger.Level.INFO, splittableRandom.ints().limit(10).boxed().toList());
        final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        log.log(System.Logger.Level.INFO, threadLocalRandom.ints(0, 100)
            .limit(10)
            .boxed()
            .toList());

        // Now 
        final RandomGeneratorFactory<RandomGenerator> random = RandomGeneratorFactory.getDefault();
        log.log(System.Logger.Level.INFO, random.name());
        log.log(System.Logger.Level.INFO, random.group());
        log.log(System.Logger.Level.INFO, random.isDeprecated());
        log.log(System.Logger.Level.INFO, random.isHardware());
        log.log(System.Logger.Level.INFO, random.isSplittable());
        log.log(System.Logger.Level.INFO, random.isJumpable());

        final RandomGenerator randomGenerator = random.create();
        log.log(System.Logger.Level.INFO, randomGenerator.ints(10).boxed().toList());

        final RandomGenerator x128MixRandom = RandomGeneratorFactory.of("L128X128MixRandom").create();
        log.log(System.Logger.Level.INFO, x128MixRandom.nextInt());
        log.log(System.Logger.Level.INFO, x128MixRandom.nextGaussian());

        final RandomGenerator.SplittableGenerator l64X128StarStarRandom = (RandomGenerator.SplittableGenerator) RandomGeneratorFactory.of("L64X128StarStarRandom").create();
        final Set<Integer> values = l64X128StarStarRandom.splits(100)
            .map(RandomGenerator::nextInt)
            .collect(Collectors.toSet())
            .parallelStream()
            .collect(Collectors.toSet());

        log.log(System.Logger.Level.INFO, values);
    }
}
