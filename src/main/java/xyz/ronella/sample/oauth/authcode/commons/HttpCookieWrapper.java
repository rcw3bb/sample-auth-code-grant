package xyz.ronella.sample.oauth.authcode.commons;

import xyz.ronella.trivial.decorator.StringBuilderAppender;

import java.io.Serial;
import java.net.HttpCookie;

/**
 * A wrapper for HttpCookie that makes to update the toString logic.
 *
 * @author Ron Webb
 */
public class HttpCookieWrapper {
    @Serial
    private static final long serialVersionUID = -4125926599943680474L;
    private transient final HttpCookie httpCookie;

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
