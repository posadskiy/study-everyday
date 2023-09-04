import static java.net.http.HttpResponse.BodyHandlers.ofString;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * JEP 321: HTTP Client
 * <p>
 * <a href="https://openjdk.org/jeps/321">Docs</a>
 * <a href="https://openjdk.org/groups/net/httpclient/recipes.html">Examples</a>
 */
public class HttpClientFeature {

    private final static Logger log = System.getLogger("default");
    private final static HttpClient httpClient = HttpClient.newHttpClient();
    private final static HttpRequest build = HttpRequest.newBuilder()
        .uri(URI.create("https://google.com"))
        .build();


    public static void main(String[] args) {
        syncRequest();
        asyncRequest();
        postRequest();
        concurrentGetRequest();
    }

    public static void syncRequest() {
        try {
            final HttpResponse<String> response = httpClient.send(build, ofString());

            log.log(Level.INFO, response.statusCode());
            log.log(Level.INFO, response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncRequest() {
        try {
            final CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = httpClient.sendAsync(build,
                ofString());

            final HttpResponse<String> response = httpResponseCompletableFuture.get();
            log.log(Level.INFO, response.statusCode());
            log.log(Level.INFO, response.body());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void postRequest() {
        var body = "{}";
        try {
            var request = HttpRequest.newBuilder()
                .uri(URI.create("https://google.com"))
                .POST(BodyPublishers.ofString(body))
                .build();

            final CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = httpClient.sendAsync(request,
                ofString());

            final HttpResponse<String> response = httpResponseCompletableFuture.get();
            log.log(Level.INFO, response.statusCode());
            log.log(Level.INFO, response.body());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void concurrentGetRequest() {
        var urls = List.of(URI.create("https://google.com"), URI.create("https://google.si"), URI.create("https://google.eu"));

        var requests = urls.stream()
            .map(HttpRequest::newBuilder)
            .map(Builder::build)
            .collect(Collectors.toList());

        final CompletableFuture<?>[] response = requests.stream()
            .map(request -> httpClient.sendAsync(request, ofString()))
            .toArray(CompletableFuture<?>[]::new);
        CompletableFuture.allOf(response)
            .join();

        Arrays.stream(response)
            .map(value -> {
                try {
                    return value.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(value -> (HttpResponse<String>) value)
            .map(HttpResponse::body)
            .peek(value -> log.log(Level.INFO, value))
            .collect(Collectors.toList());
    }

}
