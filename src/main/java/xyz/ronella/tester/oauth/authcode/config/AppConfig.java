package xyz.ronella.tester.oauth.authcode.config;

import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.trivial.handy.PathFinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Reads the application.properties file inside.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class AppConfig {

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(AppConfig.class));

    /**
     * Returns an instance of AppConfig.
     */
    public final static AppConfig INSTANCE = new AppConfig();

    final private ResourceBundle prop;

    private AppConfig() {
        try {
            final var confName = "application.properties";
            final var locations = List.of("../conf", "conf");
            final var confFound = PathFinder.getBuilder(confName).addPaths(locations).build().getFile();
            final var propFile = confFound.get();

            try(final var versionProp = new FileInputStream(propFile)) {
                this.prop = new PropertyResourceBundle(versionProp);
            }
        } catch (IOException exp) {
            LOGGER_PLUS.error(LOGGER_PLUS.getStackTraceAsString(exp));
            throw new RuntimeException(exp);
        }
    }

    /**
     * Reads the value of the server.port.
     * @return The value of the server.port as integer.
     */
    public int getServerPort() {
        final var port = prop.getString("server.port");
        return Integer.parseInt(port);
    }

    /**
     * Reads the value of the base.url.
     * @return The value of the base.url.
     */
    public String getBaseURL() {
        return prop.getString("base.url").trim();
    }
}