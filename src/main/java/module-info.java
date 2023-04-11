/**
 * The module definition.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
open module xyz.ronella.tester.oauth.authcode {
    requires static lombok;
    requires java.net.http;

    requires com.fasterxml.jackson.databind;
    requires jdk.httpserver;

    requires xyz.ronella.casual.trivial;
    requires xyz.ronella.logging.logger.plus;

    requires com.google.guice;
    requires com.google.gson;
    requires org.slf4j;
    requires org.apache.logging.log4j;

}