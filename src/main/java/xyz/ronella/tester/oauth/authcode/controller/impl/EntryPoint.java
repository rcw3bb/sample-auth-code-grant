package xyz.ronella.tester.oauth.authcode.controller.impl;

import xyz.ronella.tester.oauth.authcode.commons.Constant;
import xyz.ronella.tester.oauth.authcode.config.AppConfig;
import xyz.ronella.tester.oauth.authcode.wrapper.SimpleHttpExchange;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * This is the endpoint that redirects to the login screen of IDAM.
 * If everything is successful IDAM will redirect to the provided redirectURL.
 *
 * @author Ron Webb.
 */
public class EntryPoint extends AbstractAppResource {
    @Override
    public void process(SimpleHttpExchange simpleExchange) {
        final var appConfig = AppConfig.INSTANCE;
        final var authURL = appConfig.getAuthURL();
        final var clientId = appConfig.getClientId();
        final var redirectURL = URLEncoder.encode(appConfig.getRedirectURL(), StandardCharsets.UTF_8);
        final var state = UUID.randomUUID().toString();
        final var redirectUrl = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s&state=%s", authURL, clientId, redirectURL, state);

        simpleExchange.setAttribute(Constant.ATTR_STATE, state);
        simpleExchange.redirect(redirectUrl);
    }

    @Override
    public String getPathPattern() {
        return String.format("^%s/entry", getBaseURL());
    }
}
