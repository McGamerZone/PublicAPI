package zone.themcgamer.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zone.themcgamer.api.json.JsonRequest;
import zone.themcgamer.api.model.AccountModel;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter
public class McGamerZoneAPI {
    private static boolean initialized;
    @Getter private static String url;

    private final String apiKey;
    private final APIVersion apiVersion;
    private final HttpClient httpClient;

    /**
     * Get the {@link AccountModel} by the given {@link UUID} with blocking
     *
     * @param uuid the uuid to get the account for
     * @return the account
     */
    public AccountModel getAccount(@NonNull UUID uuid) {
        return AccountModel.fromResponse(new JsonRequest(url + "/account/" + uuid.toString(), this).getResponse());
    }

    /**
     * Get the {@link AccountModel} by the given {@link UUID} asynchronously
     *
     * @param uuid the uuid to get the account for
     * @param consumer the consumer the account will be accepted in
     */
    public void getAccountAsync(@NonNull UUID uuid, @NonNull Consumer<AccountModel> consumer) {
        new JsonRequest(url + "/account/" + uuid.toString(), this)
                .getResponseAsync(jsonResponse -> consumer.accept(AccountModel.fromResponse(jsonResponse)));
    }

    /**
     * Create a new {@link McGamerZoneAPIBuilder} with the given api key
     *
     * @param apiKey the api key
     * @return the builder
     */
    public static McGamerZoneAPIBuilder builder(String apiKey) {
        return new McGamerZoneAPIBuilder(apiKey);
    }

    @RequiredArgsConstructor @Slf4j(topic = "McGamerZoneAPI")
    public static class McGamerZoneAPIBuilder {
        @NonNull private final String apiKey;
        private APIVersion apiVersion = APIVersion.V1;

        /**
         * Set the {@link APIVersion} to use
         *
         * @param apiVersion the api version
         */
        public McGamerZoneAPIBuilder withVersion(APIVersion apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        /**
         * Build the {@link McGamerZoneAPI}
         *
         * @return the built api
         */
        public McGamerZoneAPI build() {
            if (initialized)
                throw new IllegalStateException("The API has already been initialized");
            if (apiKey.length() != 36 || (!apiKey.contains("-")))
                throw new IllegalArgumentException("Invalid api key defined");
            initialized = true;
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .connectTimeout(Duration.ofSeconds(30L))
                    .build();
            url = "https://api.mcgamerzone.net/" + apiVersion.getName();

            int dashIndex = apiKey.indexOf("-");
            String hiddenApiKey = apiKey.substring(0, dashIndex) + "*".repeat(apiKey.substring(dashIndex, apiKey.length() - 1).length());

            log.info("McGamerZone API Details:");
            log.info("  API Key: " + hiddenApiKey);
            log.info("  Version: " + apiVersion.getName());
            log.info("  URL: " + url);
            return new McGamerZoneAPI(apiKey, apiVersion, httpClient);
        }
    }
}