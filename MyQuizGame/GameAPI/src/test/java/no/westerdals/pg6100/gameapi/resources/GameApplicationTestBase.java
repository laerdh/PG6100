package no.westerdals.pg6100.gameapi.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.gameapi.core.Game;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class GameApplicationTestBase {

    protected static WireMockServer wiremockServer;

    @BeforeClass
    public static void initRestAssured() {
        // Set up RestAssured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/game/api/games";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Run Wiremock as a process binding on port 8099
        wiremockServer = new WireMockServer(
                wireMockConfig().port(8080).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();
    }

    @AfterClass
    public static void tearDown() {
        wiremockServer.stop();
    }

    @Before
    @After
    public void clean() {
        List<Game> games = Arrays.asList(given().accept(ContentType.JSON)
                .queryParam("n", 20)
                .get()
                .then()
                .statusCode(200)
                .extract().as(Game[].class));

        games.forEach(g ->
            given().pathParam("id", g.getId())
                    .delete("/{id}")
                    .then()
                    .statusCode(204));
    }
}