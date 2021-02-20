package zone.themcgamer.api.json;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zone.themcgamer.api.McGamerZoneAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Consumer;

/**
 * @author Braydon
 */
@AllArgsConstructor @Slf4j(topic = "Json Request")
public class JsonRequest {
    private final String url;
    private final McGamerZoneAPI mcGamerZoneAPI;

    /**
     * Get the {@link JsonResponse} from the provided url
     *
     * @return the response
     */
    public JsonResponse getResponse() {
        try {
            return checkResponse(mcGamerZoneAPI.getHttpClient().send(buildRequest(), HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Get the {@link JsonResponse} from the provided url asynchronously
     *
     * @param consumer the consumer the response will be accepted in
     */
    public void getResponseAsync(Consumer<JsonResponse> consumer) {
        mcGamerZoneAPI.getHttpClient().sendAsync(buildRequest(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(httpResponse -> consumer.accept(checkResponse(httpResponse)));
    }

    private JsonResponse checkResponse(HttpResponse<String> httpResponse) {
        int statusCode = httpResponse.statusCode();
        if (statusCode != 200) {
            log.warn("Response from \"" + url + "\" returned status code " + statusCode + ":");
            log.warn("Body: " + httpResponse.body());
            log.warn("Headers: " + httpResponse.headers().toString());
        }
        return new JsonResponse(httpResponse);
    }

    private HttpRequest buildRequest() {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1L))
                .header("Content-Type", "application/json")
                .header("key", mcGamerZoneAPI.getApiKey())
                .build();
    }
}