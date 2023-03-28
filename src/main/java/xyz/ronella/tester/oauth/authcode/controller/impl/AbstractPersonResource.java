package xyz.ronella.tester.oauth.authcode.controller.impl;

import xyz.ronella.tester.oauth.authcode.config.AppConfig;
import xyz.ronella.tester.oauth.authcode.config.PersonModule;
import xyz.ronella.tester.oauth.authcode.model.Person;
import xyz.ronella.tester.oauth.authcode.service.IPersonService;
import xyz.ronella.tester.oauth.authcode.controller.IResource;
import xyz.ronella.tester.oauth.authcode.wrapper.SimpleHttpExchange;

import xyz.ronella.tester.oauth.authcode.wrapper.SimpleJson;
import xyz.ronella.trivial.handy.RegExMatcher;

import java.util.List;
import java.util.regex.Matcher;

/**
 * A partial implementation of IResource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public abstract class AbstractPersonResource implements IResource {

    /**
     * An instance of IPersonService.
     */
    private IPersonService personService;
    private Matcher pathMatcher;

    public IPersonService getService() {
        return personService;
    }

    /**
     * The base url configured in application.properties.
     * @return The base url.
     */
    protected String getBaseURL() {
        return AppConfig.INSTANCE.getBaseURL();
    }

    /**
     * The default constructor that must be called from the subclass.
     */
    public AbstractPersonResource() {
        this.personService = PersonModule.getInstance(IPersonService.class);
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
        return String.format("^%s/person$", getBaseURL());
    }

    /**
     * Converts the Person object to json text.
     * @param person An instance of Person.
     * @return The json text.
     */
    protected String personToJson(Person person) {
        final var mapper = new SimpleJson<Person>();
        return mapper.toJsonText(person);
    }

    /**
     * Coverts a list of Person object to json text.
     * @param persons A list of Person object.
     * @return The json text.
     */
    protected String personListToJson(final List<Person> persons) {
        final var mapper = new SimpleJson<List<Person>>();
        return mapper.toJsonText(persons);
    }

    /**
     * Converts a json text to Person object.
     * @param json The json text.
     * @return An instance of Person object.
     */
    protected Person jsonToPerson(String json) {
        final var simpleJson = new SimpleJson<Person>();
        return simpleJson.toObjectType(json, Person.class);
    }
}
