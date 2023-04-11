package xyz.ronella.sample.oauth.authcode.controller.impl;

import xyz.ronella.sample.oauth.authcode.commons.ResponseStatus;
import xyz.ronella.sample.oauth.authcode.commons.Constant;
import xyz.ronella.sample.oauth.authcode.config.AppConfig;
import xyz.ronella.sample.oauth.authcode.wrapper.SimpleHttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * The endpoint the is responsible for exchanging the authorization token to access token.
 *
 * @author Ron Webb
 */
public class Callback extends AbstractAppResource {

    private String createRequestBody(final String authCode) {
        final var appConfig = AppConfig.INSTANCE;

        final var redirectUri = appConfig.getRedirectURL();

        final var params = new HashMap<String, String>();
        params.put("grant_type", "authorization_code");
        params.put("code", authCode);
        params.put("redirect_uri", redirectUri);

        return params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    private String getBasicAuth() {
        final var appConfig = AppConfig.INSTANCE;
        final var clientId = appConfig.getClientId();
        final var clientSecret = appConfig.getClientSecret();
        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    private HttpRequest createHTTPRequest(final String authCode) {
        final var tokenEndpoint = AppConfig.INSTANCE.getTokenURL();

        return HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Authorization", "Basic " + getBasicAuth())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(createRequestBody(authCode)))
                .build();
    }

    @Override
    public void process(SimpleHttpExchange simpleExchange) {
        final var authCode = simpleExchange.getRequestParameter(Constant.RQT_PARAM_CODE);
        final var state = simpleExchange.getRequestParameter(Constant.RQT_PARAM_STATE);

        if (authCode!=null && state!=null && state.equals(simpleExchange.getStringAttribute(Constant.ATTR_STATE))) {
            final var request = createHTTPRequest(authCode);
            final var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
            try {
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
        return String.format("^%s/callback", getBaseURL());
    }

}
