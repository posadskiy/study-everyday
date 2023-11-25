package com.posadskiy.java.release.v17;

import lombok.extern.log4j.Log4j2;

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
@Log4j2
public class RandomGenerators {

    public static void main(String[] args) {
        // Before 
        final SplittableRandom splittableRandom = new SplittableRandom();
        log.info(splittableRandom.ints().limit(10).boxed().toList());
        final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        log.info(threadLocalRandom.ints(0, 100)
            .limit(10)
            .boxed()
            .toList());

        // Now 
        final RandomGeneratorFactory<RandomGenerator> random = RandomGeneratorFactory.getDefault();
        log.info(random.name());
        log.info(random.group());
        log.info(random.isDeprecated());
        log.info(random.isHardware());
        log.info(random.isSplittable());
        log.info(random.isJumpable());

        final RandomGenerator randomGenerator = random.create();
        log.info(randomGenerator.ints(10).boxed().toList());

        final RandomGenerator x128MixRandom = RandomGeneratorFactory.of("L128X128MixRandom").create();
        log.info(x128MixRandom.nextInt());
        log.info(x128MixRandom.nextGaussian());

        final RandomGenerator.SplittableGenerator l64X128StarStarRandom = (RandomGenerator.SplittableGenerator) RandomGeneratorFactory.of("L64X128StarStarRandom").create();
        final Set<Integer> values = l64X128StarStarRandom.splits(100)
            .map(RandomGenerator::nextInt)
            .collect(Collectors.toSet())
            .parallelStream()
            .collect(Collectors.toSet());

        log.info(values);
    }
}
