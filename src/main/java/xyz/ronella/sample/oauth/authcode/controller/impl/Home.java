package xyz.ronella.sample.oauth.authcode.controller.impl;

import xyz.ronella.sample.oauth.authcode.commons.Constant;
import xyz.ronella.sample.oauth.authcode.commons.ResponseStatus;
import xyz.ronella.sample.oauth.authcode.wrapper.SimpleHttpExchange;

/**
 * The home page of the protected resource.
 *
 * @author Ron Webb
 */
public class Home extends AbstractAppResource {

    @Override
    public void process(SimpleHttpExchange simpleExchange) {
        final var accessTokenCookie = simpleExchange.getCookie(Constant.ACCESS_TOKEN);

        if (accessTokenCookie.isEmpty()) {
            simpleExchange.redirect(Constant.URL_REFRESH);
        }
        else {

            //It is necessary to test the value of sessionState before forwarding to home page logic.
            //If the sessionState is not the expected value redirect it entry page.
            //However, for this sample we will only check for its existence for simplicity.
            final var sessionState = simpleExchange.getCookie(Constant.SESSION_STATE);

            if (sessionState.isPresent()) {
                homePage(simpleExchange);
            }
            else {
                simpleExchange.redirect(Constant.URL_ENTRY);
            }
        }
    }

    public void homePage(SimpleHttpExchange simpleExchange) {
        simpleExchange.sendResponseText(ResponseStatus.OK, "This is a protected page.");
    }


    @Override
    public String getPathPattern() {
        return String.format("^%s/", getBaseURL());
    }
}