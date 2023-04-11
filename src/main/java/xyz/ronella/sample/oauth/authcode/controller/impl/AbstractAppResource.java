package xyz.ronella.sample.oauth.authcode.controller.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import xyz.ronella.sample.oauth.authcode.commons.HttpCookieWrapper;
import xyz.ronella.sample.oauth.authcode.config.AppConfig;
import xyz.ronella.sample.oauth.authcode.commons.Constant;
import xyz.ronella.sample.oauth.authcode.controller.IResource;
import xyz.ronella.sample.oauth.authcode.wrapper.SimpleHttpExchange;
import xyz.ronella.trivial.handy.RegExMatcher;
import java.net.HttpCookie;
import java.util.regex.Matcher;

/**
 * A partial implementation of IResource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public abstract class AbstractAppResource implements IResource {

    private Matcher pathMatcher;

    /**
     * The base url configured in application.properties.
     * @return The base url.
     */
    protected String getBaseURL() {
        return AppConfig.INSTANCE.getBaseURL();
    }

    /**
     * The Matcher basted on path pattern.
     * @param simpleExchange An instance of SimpleHttpExchange
     * @return An instance of Matcher.
     */
    protected Matcher getPathMatcher(final SimpleHttpExchange simpleExchange) {
        if (null == pathMatcher) {
            pathMatcher = RegExMatcher.find(getPathPattern(), simpleExchange.getRequestPath());
        }

        return pathMatcher;
    }

    /**
     * The default logic canProcess logic.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true if this particular implementation the target path pattern.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        return getPathMatcher(simpleExchange).matches();
    }

    /**
     * The default path pattern implementation.
     * @return The path pattern.
     */
    @Override
    public String getPathPattern() {
        return String.format("^%s/", getBaseURL());
    }

    /**
     * Redirect to home based on the response after requesting for an access token.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @param responseBody The response body in JSON format.
     */
    protected void redirectToHome(final SimpleHttpExchange simpleExchange, final String responseBody) {
        final var gson = new Gson();

        final var tokenJson = gson.fromJson(responseBody, JsonObject.class);

        final var accessToken = tokenJson.get("access_token").getAsString();
        final var expiresIn = tokenJson.get("expires_in").getAsLong();

        final var refreshToken = tokenJson.get("refresh_token").getAsString();
        final var refresh_expires_in = tokenJson.get("refresh_expires_in").getAsLong();
        final var sessionState = tokenJson.get("session_state").getAsString();

        //Access token is the only possible token to be in browser cookies.
        final var accessTokenCookie = createCookie(Constant.ACCESS_TOKEN, accessToken, expiresIn);

        //Just for this sample we are storing refresh token and session state in browser cookies for simplicity.
        //For actual usage these should be persisted somewhere (e.g. database).
        final var refreshTokenCookie = createCookie(Constant.REFRESH_TOKEN, refreshToken, refresh_expires_in);
        final var sessionStateCookie = createCookie(Constant.SESSION_STATE, sessionState, refresh_expires_in);

        simpleExchange.redirect(Constant.URL_HOME, sessionStateCookie, accessTokenCookie, refreshTokenCookie);
    }

    private String createCookie(final String name, final String value, final long maxAge) {
        final var cookie = new HttpCookie(name, value);
        if (maxAge>-1) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return new HttpCookieWrapper(cookie).toString();
    }
}
