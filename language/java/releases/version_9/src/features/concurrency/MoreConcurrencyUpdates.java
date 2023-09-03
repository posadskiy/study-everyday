package features.concurrency;

import java.lang.System.Logger.Level;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JEP 266: More Concurrency Updates
 * <p>
 * <a href="http://www.reactive-streams.org">Reactive streams docs</a>
 * <a href="https://github.com/reactive-streams/reactive-streams-jvm/tree/v1.0.4/examples/src">Reactive streams example</a>
 */
public class MoreConcurrencyUpdates {

    final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {

        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hola!")
            .thenApplyAsync(value -> {
                System.getLogger("default").log(Level.INFO, Thread.currentThread().getName());

                return value;
            }, executorService);

        try {
            System.getLogger("default").log(Level.INFO, future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.getLogger("default").log(Level.INFO, future.isDone());

        executorService.shutdown();
    }

}

