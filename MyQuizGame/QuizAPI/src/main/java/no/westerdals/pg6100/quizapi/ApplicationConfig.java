package no.westerdals.pg6100.quizapi;

import io.swagger.jaxrs.config.BeanConfig;
import no.westerdals.pg6100.quizapi.api.implementation.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/quiz/api")
public class ApplicationConfig extends Application {

    private final Set<Class<?>> classes;

    public ApplicationConfig() {

        // Configure Swagger to create API documentation
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.2.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/myquizgame/quiz/api");
        beanConfig.setResourcePackage("no.westerdals.pg6100.quizapi");

        // Initialize swagger
        beanConfig.setScan(true);

        // Define which classes to provide RestAPI
        HashSet<Class<?>> c = new HashSet<>();
        c.add(CategoryRest.class);
        c.add(SubCategoryRest.class);
        c.add(SubSubCategoryRest.class);
        c.add(QuizRest.class);
        c.add(RandomQuizRest.class);
        c.add(RandomQuizzesRest.class);

        // Add further configuration to activate Swagger
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
