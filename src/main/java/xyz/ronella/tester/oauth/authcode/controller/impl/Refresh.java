package xyz.ronella.tester.oauth.authcode.controller.impl;

import xyz.ronella.tester.oauth.authcode.commons.Constant;
import xyz.ronella.tester.oauth.authcode.commons.ResponseStatus;
import xyz.ronella.tester.oauth.authcode.config.AppConfig;
import xyz.ronella.tester.oauth.authcode.wrapper.SimpleHttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * The endpoint that handles the refresh of the access token.
 *
 * @author Ron Webb
 */
public class Refresh extends AbstractAppResource {

    private String createRequestBody(final String refreshToken) {
        final var appConfig = AppConfig.INSTANCE;

        final var clientId = appConfig.getClientId();
        final var clientSecret = appConfig.getClientSecret();

        final var params = new HashMap<String, String>();
        params.put("grant_type", "refresh_token");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("refresh_token", refreshToken);

        return params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    @Override
    public void process(SimpleHttpExchange simpleExchange) {
        final var refreshToken = simpleExchange.getCookie(Constant.REFRESH_TOKEN);

        if (refreshToken.isPresent()) {
            final var tokenEndpoint = AppConfig.INSTANCE.getTokenURL();

            final var request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenEndpoint))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(refreshToken.get().getValue())))
                    .build();
            try {
                final var client = HttpClient.newHttpClient();

                final var response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == ResponseStatus.OK.getCode()) {
                    redirectToHome(simpleExchange, response.body());
                }
            } catch (IOException | InterruptedException exception) {
                simpleExchange.sendResponseText(ResponseStatus.BAD_REQUEST, "Error authorizing");
                throw new RuntimeException(exception);
            }
        }
        else {
            simpleExchange.redirect(Constant.URL_ENTRY);
        }
    }

    @Override
    public String getPathPattern() {
        return String.format("^%s/refresh", getBaseURL());
    }
}
