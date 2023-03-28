package xyz.ronella.tester.oauth.authcode.config;

import com.google.inject.AbstractModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import xyz.ronella.tester.oauth.authcode.controller.impl.*;
import xyz.ronella.tester.oauth.authcode.service.IPersonService;
import xyz.ronella.tester.oauth.authcode.controller.IResource;
import xyz.ronella.tester.oauth.authcode.controller.IResources;
import xyz.ronella.tester.oauth.authcode.repository.IPersonRepository;
import xyz.ronella.tester.oauth.authcode.repository.impl.PersonListRepository;
import xyz.ronella.tester.oauth.authcode.service.impl.PersonServiceImpl;

/**
 * The configuration to wiring all Person related resources.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class PersonModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IPersonService.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonServiceImpl.class);
        bind(IPersonRepository.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonListRepository.class);
        bind(IResources.class)
                .annotatedWith(Names.named(PersonResources.RESOURCE_NAME))
                .to(PersonResources.class);

        final var resourceBinder = Multibinder.newSetBinder(binder(), IResource.class, Names.named(PersonResources.RESOURCE_NAME));
        resourceBinder.addBinding().to(PersonCreate.class);
        resourceBinder.addBinding().to(PersonDeleteById.class);
        resourceBinder.addBinding().to(PersonRetrieveAll.class);
        resourceBinder.addBinding().to(PersonRetrieveById.class);
        resourceBinder.addBinding().to(PersonUpdateById.class);
    }

    private static Injector getInjector() {
        return Guice.createInjector(new PersonModule());
    }

    /**
     * Returns an instance of the target interface that is fully wired.
     * @param clazz The target class.
     * @return An instance of the wired class.
     * @param <T> The target actual time.
     */
    public static <T> T getInstance(final Class<T> clazz) {
        return getInjector().getInstance(Key.get(clazz, Names.named(PersonResources.RESOURCE_NAME)));
    }

}
