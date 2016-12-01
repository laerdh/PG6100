package no.westerdals.pg6100.rest;

import io.swagger.jaxrs.config.BeanConfig;
import no.westerdals.pg6100.rest.api.implementation.UserRest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/workshop/api")
public class ApplicationConfig extends Application {

    public static final String BASE_PATH = "/workshop/api";

    private final Set<Class<?>> classes;

    public ApplicationConfig() {

//        // Configure Swagger to create API Documentation
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.0.1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/workshop/api");
        beanConfig.setResourcePackage("no.westerdals.pg6100.rest");

        // Initialize swagger
        beanConfig.setScan(true);

        // Define which classes to provide RestAPI
        HashSet<Class<?>> c = new HashSet<>();
        c.add(UserRest.class);

//        // Add further configuration to activate Swagger
        c.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        c.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        // Needed to handle dates in Java 8
        c.add(ObjectMapperContextResolver.class);
        classes = Collections.unmodifiableSet(c);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
