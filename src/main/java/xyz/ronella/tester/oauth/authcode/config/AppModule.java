package xyz.ronella.tester.oauth.authcode.config;

import com.google.inject.AbstractModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import xyz.ronella.tester.oauth.authcode.controller.impl.*;
import xyz.ronella.tester.oauth.authcode.controller.IResource;
import xyz.ronella.tester.oauth.authcode.controller.IResources;

/**
 * The configuration to wiring all application related resources.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IResources.class)
                .annotatedWith(Names.named(AppResources.RESOURCE_NAME))
                .to(AppResources.class);

        final var resourceBinder = Multibinder.newSetBinder(binder(), IResource.class, Names.named(AppResources.RESOURCE_NAME));
        resourceBinder.addBinding().to(EntryPoint.class);
        resourceBinder.addBinding().to(Callback.class);
        resourceBinder.addBinding().to(Home.class);
        resourceBinder.addBinding().to(Refresh.class);
    }

    private static Injector getInjector() {
        return Guice.createInjector(new AppModule());
    }

    /**
     * Returns an instance of the target interface that is fully wired.
     * @param clazz The target class.
     * @return An instance of the wired class.
     * @param <T> The target actual time.
     */
    public static <T> T getInstance(final Class<T> clazz) {
        return getInjector().getInstance(Key.get(clazz, Names.named(AppResources.RESOURCE_NAME)));
    }

}
