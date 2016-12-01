package no.westerdals.pg6100.gameapi.resources;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.RestAssured;
import no.westerdals.pg6100.gameapi.GameApplication;
import no.westerdals.pg6100.gameapi.GameConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public class GameRestIT {

    private static final String EXTERNAL_API_BASE_PATH = "/myquizgame/quiz/api";
    private static WireMockServer wiremockServer;

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class);


    @BeforeClass
    public static void initClass() {
        // Set up RestAssured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        RestAssured.basePath = "/game/api/games";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Run Wiremock as a process binding on port 8099
        wiremockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();
    }

    @AfterClass
    public static void tearDown() {
        wiremockServer.stop();
    }


    private void stubJsonQuizResponse(String json) throws   Exception {
        wiremockServer.stubFor(
                WireMock.post(
                        urlMatching(EXTERNAL_API_BASE_PATH + "/randomQuizzes"))
                        .withQueryParam("n", WireMock.containing("2"))
                        .withQueryParam("filter", WireMock.containing("1"))

                        .willReturn(WireMock.aResponse()
                            .withHeader("Content-Type", "application/json; charset=utf-8")
                            .withHeader("Content-Length", "" + json.getBytes("utf-8").length)));
    }

    private String getQuizListJson() {
        String json = "[";
        json += "{";
        json += "\"id\": 4,";
        json += "\"question\": \"Who won PL 14/15?\",";
        json += "\"answers\": [";
        json += "\"Chelsea FC\",";
        json += "\"Manchester United\",";
        json += "\"Liverpool\",";
        json += "\"Arsenal\"";
        json += "],";

        json += "NEXT JSON OBJECT GOES HERE!";

        return json;
    }
}