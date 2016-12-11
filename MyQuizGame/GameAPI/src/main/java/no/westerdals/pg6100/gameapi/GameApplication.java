package no.westerdals.pg6100.gameapi;

import com.netflix.config.ConfigurationManager;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import no.westerdals.pg6100.gameapi.dao.GameDao;
import no.westerdals.pg6100.gameapi.resources.GameRest;
import org.apache.commons.configuration.AbstractConfiguration;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/*
    Dropwizard uses:

    - Jetty: for servlet implementation
    - Jersey: for JAX-RS (in contrast to RestEasy in Wildfly)
    - Jackson: for Json un/marshaling (same as Wildfly)
 */
public class GameApplication extends Application<GameConfiguration> {

    public static final String API_PATH = "/game/api";

    public static void main(String[] args) throws Exception {
        new GameApplication().run(args);
    }

    @Override
    public String getName() {
        return "Game API for MyQuizGame written in DropWizard";
    }

    @Override
    public void initialize(Bootstrap<GameConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/game", "index.html", "a"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "b"));
        bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "c"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images", null, "d"));
        bootstrap.addBundle(new AssetsBundle("/assets/lang", "/lang", null, "e"));
        bootstrap.addBundle(new AssetsBundle("/assets/lib", "/lib", null, "f"));
    }

    @Override
    public void run(GameConfiguration gameConfiguration, Environment environment) throws Exception {

        // DB config
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, gameConfiguration.getDatabase(), "h2");

        // Init H2 testdata
        createTestData(jdbi);

        final GameDao gameDao = jdbi.onDemand(GameDao.class);
        final GameRest gameResource = new GameRest(gameDao);

        environment.jersey().setUrlPattern(API_PATH + "/*");

        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(gameResource);

        // Swagger configuration
        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());

        BeanConfig config = new BeanConfig();
        config.setVersion("0.0.1");
        config.setHost("localhost:8081");
        config.setBasePath(API_PATH);
        config.setResourcePackage("no.westerdals.pg6100.gameapi");
        config.setScan(true);

        // Hystrix configuration
        AbstractConfiguration conf = ConfigurationManager.getConfigInstance();
        // How long to wait before giving up a request?
        conf.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 500);
        // How many failures before activating circuit breaker?
        conf.setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold", 2);
        conf.setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage", 50);
        // For how long should the circuit breaker stop the requests?
        // After this, 1 single request will try to check if the remote server is ok
        conf.setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds", 5000);
    }

    private void createTestData(DBI dbi) {
        try (Handle handle = dbi.open()) {
            handle.createCall("" +
                    "CREATE TABLE IF NOT EXISTS GAME" +
                    "(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "quizzes VARCHAR(128)," +
                    "answered INT," +
                    "totalQuizzes INT" +
                    ");").invoke();
//
//            handle.createStatement("" +
//                    "INSERT INTO GAME (quizzes, answered)" +
//                    "VALUES (?, ?);")
//                    .bind(0, "{ \"quizzes\" : [4,2,5,9,3,1,11] }")
//                    .bind(1, 0).execute();
//
//            handle.createStatement("" +
//                    "INSERT INTO GAME (quizzes, answered)" +
//                    "VALUES (?, ?);")
//                    .bind(0, "{ \"quizzes\" : [4,11,14,23,32,35] }")
//                    .bind(1, 0).execute();
//
//            handle.createStatement("" +
//                    "INSERT INTO GAME (quizzes, answered)" +
//                    "VALUES (?, ?);")
//                    .bind(0, "{ \"quizzes\" : [1,95,34,21,58,38,59,23] }")
//                    .bind(1, 0).execute();
        }
    }
}
