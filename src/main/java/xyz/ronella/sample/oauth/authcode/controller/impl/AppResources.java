package xyz.ronella.sample.oauth.authcode.controller.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.sample.oauth.authcode.config.AppModule;
import xyz.ronella.sample.oauth.authcode.controller.IResource;
import xyz.ronella.sample.oauth.authcode.controller.IResources;
import xyz.ronella.sample.oauth.authcode.wrapper.SimpleHttpExchange;

import java.util.Optional;
import java.util.Set;

/**
 * The implementation for IResources for returning a particular implementation of IResource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class AppResources implements IResources {

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(AppResources.class));

    /**
     * The resource name for Application.
     */
    public static final String RESOURCE_NAME = "Application";

    final private Set<IResource> resources;

    /**
     * Creates an instance of AppResources.
     * @param resources An set of unique implementation of IResource.
     */
    @Inject
    public AppResources(@Named(RESOURCE_NAME) final Set<IResource> resources) {
        this.resources = resources;
    }

    /**
     * A set of unique implementation if IResource.
     * @return A set of IResource.
     */
    @Override
    public Set<IResource> getResources() {
        return resources;
    }

    /**
     * Creates a particular implementation IResource.
     * @param exchange An instance of SimpleHttpExchange.
     * @return An implementation of IResource.
     */
    public static Optional<IResource> createResource(SimpleHttpExchange exchange) {
        try(var mLOG = LOGGER_PLUS.groupLog("Optional<IResource> getInstance(SimpleHttpExchange)")) {
            final var appResources = AppModule.getInstance(IResources.class);
            final var resources = appResources.getResources();
            final var resource = resources.stream().filter(___resource -> ___resource.canProcess(exchange)).findFirst();
            mLOG.debug(()-> "Resource instance: " + resource.get());
            return resource;
        }
    }

}
