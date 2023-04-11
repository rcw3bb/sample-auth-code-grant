package xyz.ronella.sample.oauth.authcode.controller;

import java.util.Set;

/**
 * Must hold the logic to return all the active IResource implementation.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public interface IResources {

    /**
     * Must hold the logic to return all the active implementation of IResource.
     * @return A set of instance IResource implementation.
     */
    Set<IResource> getResources();

}
