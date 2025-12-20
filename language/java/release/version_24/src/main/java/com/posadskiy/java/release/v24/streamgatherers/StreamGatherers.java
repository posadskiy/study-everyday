package com.posadskiy.java.release.v24.streamgatherers;


import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/// ### JEP 485: Stream Gatherers
///
/// Enhance the Stream API to support custom intermediate operations. This will allow stream pipelines to transform data in ways that are not easily achievable with the existing built-in intermediate operations.
///
/// [Link](https://openjdk.org/jeps/485)
///
@Log4j2
public class StreamGatherers {
    public static void main(String[] args) {
        var result = Stream.of("foo", "bar", "baz", "quux")
            .gather(new Gatherer<String, List<Object>, Object>() {

                @Override
                public Supplier<List<Object>> initializer() {
                    log.info("Initialization");
                    return ArrayList::new;
                }

                @Override
                public BinaryOperator<List<Object>> combiner() {
                    return (left, right) -> {
                        log.info("Combiner: {} {}", left, right);
                        return left;
                    };
                }

                @Override
                public Integrator<List<Object>, String, Object> integrator() {
                    return ((state, element, downstream) -> {
                        state.add(element);
                        downstream.push(element.length());
                        log.info(state.toString());
                        return true;
                    });
                }

                @Override
                public BiConsumer<List<Object>, Downstream<? super Object>> finisher() {
                    return ((objects, downstream) -> {
                        log.info("Final objects: {}", objects.toString());
                    });
                }
            })
            .toList();

        log.info(result.toString());
    }
}
