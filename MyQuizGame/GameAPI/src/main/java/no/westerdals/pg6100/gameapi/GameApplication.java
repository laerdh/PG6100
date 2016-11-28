package no.westerdals.pg6100.gameapi;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import no.westerdals.pg6100.gameapi.dao.GameDao;
import no.westerdals.pg6100.gameapi.resources.GameRest;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/*
    Dropwizard uses:

    - Jetty: for servlet implementation
    - Jersey: for JAX-RS (in contrast to RestEasy in Wildfly)
    - Jackson: for Json un/marshaling (same as Wildfly)
 */
public class GameApplication extends Application<GameConfiguration> {

    public static void main(String[] args) throws Exception {
        new GameApplication().run(args);
    }

    @Override
    public String getName() {
        return "Game API for Quiz written in DropWizard";
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
        final DBI jdbi = factory.build(environment, gameConfiguration.getDataSourceFactory(), "h2");

        // Init H2 testdata
        createTestData(jdbi);

        final GameDao gameDao = jdbi.onDemand(GameDao.class);
        final GameRest gameResource = new GameRest(gameDao);

        environment.jersey().setUrlPattern("/game/api/*");

        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(gameResource);

        // Swagger

        environment.jersey().register(new io.swagger.jaxrs.listing.SwaggerSerializers());

        BeanConfig config = new BeanConfig();
        config.setVersion("0.0.1");
        config.setHost("localhost:8081");
        config.setBasePath("/game/api/");
        config.setResourcePackage("no.westerdals.pg6100.gameapi");
        config.setScan(true);
    }

    private void createTestData(DBI dbi) {
        try (Handle handle = dbi.open()) {
            handle.createCall("" +
                    "CREATE TABLE GAME" +
                    "(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "quizzes VARCHAR(128)," +
                    "answered INT," +
                    "totalQuizzes INT" +
                    ");").invoke();

        }
    }
}
