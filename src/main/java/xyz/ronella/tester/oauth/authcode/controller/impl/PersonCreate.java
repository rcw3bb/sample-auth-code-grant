package xyz.ronella.tester.oauth.authcode.controller.impl;

import xyz.ronella.tester.oauth.authcode.commons.ContentType;
import xyz.ronella.tester.oauth.authcode.commons.Method;
import xyz.ronella.tester.oauth.authcode.wrapper.SimpleHttpExchange;

/**
 * A resource implementation for creating a Person.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonCreate extends AbstractPersonResource {

    /**
     * Creates an instance of PersonCreate.
     */
    public PersonCreate() {
        super();
    }

    /**
     * The logic for declaring that it can do processing for creating a Person resource.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true to process.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        final var method = simpleExchange.getRequestMethod().orElse(Method.GET);
        final var contentType = simpleExchange.getRequestContentType();

        return super.canProcess(simpleExchange) && Method.POST.equals(method) && ContentType.APPLICATION_JSON.equals(contentType.get());
    }

    /**
     * The logic for creating a Person resource.
     * @param simpleExchange An instance of SimpleHttpExchange.
     */
    @Override
    public void process(final SimpleHttpExchange simpleExchange) {
        final var payload = simpleExchange.getRequestPayload();
        final var person = jsonToPerson(payload);
        final var createdPerson = getService().create(person);
        final var response = personToJson(createdPerson);

        simpleExchange.sendJsonResponse(response);
    }

}
