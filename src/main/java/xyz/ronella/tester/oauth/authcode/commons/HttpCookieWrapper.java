package xyz.ronella.tester.oauth.authcode.commons;

import xyz.ronella.trivial.decorator.StringBuilderAppender;

import java.net.HttpCookie;

/**
 * A wrapper for HttpCookie that makes to update the toString logic.
 *
 * @author Ron Webb
 */
public class HttpCookieWrapper {
    private final HttpCookie httpCookie;

    public HttpCookieWrapper(final HttpCookie httpCookie) {
        this.httpCookie = httpCookie;
    }

    public String toString() {

        final var sb = new StringBuilderAppender()
                .append(httpCookie.toString())
                .append(httpCookie::isHttpOnly, ";httponly")
                .append(httpCookie::getSecure, ";secure")
                .append(() -> httpCookie.getMaxAge()>0, String.format(";max-age=%d", httpCookie.getMaxAge()));

        return sb.toString();
    }

}
