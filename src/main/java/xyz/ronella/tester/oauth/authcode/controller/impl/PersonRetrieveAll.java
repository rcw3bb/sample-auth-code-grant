package xyz.ronella.tester.oauth.authcode.controller.impl;

import xyz.ronella.tester.oauth.authcode.commons.Method;
import xyz.ronella.tester.oauth.authcode.wrapper.SimpleHttpExchange;

/**
 * A resource implementation for retrieving all the Persons.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonRetrieveAll extends AbstractPersonResource {

    /**
     * Creates an instance of PersonRetrieveAll.
     */
    public PersonRetrieveAll() {
        super();
    }

    /**
     * The logic for declaring that it can do processing for returning all the Persons.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true to process.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        final var method = simpleExchange.getRequestMethod().orElse(Method.GET);
        return super.canProcess(simpleExchange) && Method.GET.equals(method);
    }

    /**
     * The logic for retrieving all the Persons.
     * @param simpleExchange An instance of SimpleHttpExchange.
     */
    @Override
    public void process(final SimpleHttpExchange simpleExchange) {
        final var persons = getService().findAll();
        final var response = personListToJson(persons);

        simpleExchange.sendJsonResponse(response);
    }

}
