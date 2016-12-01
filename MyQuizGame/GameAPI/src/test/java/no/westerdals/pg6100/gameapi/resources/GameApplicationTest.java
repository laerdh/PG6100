package no.westerdals.pg6100.gameapi.resources;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.gameapi.GameApplication;
import no.westerdals.pg6100.gameapi.GameConfiguration;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.core.Is.is;

public class GameApplicationTest extends GameApplicationTestBase {

    private static final String FILE_PATH = "src/test/java/no/westerdals/pg6100/gameapi/resources/testdata/";

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class, "config.yml");

    @Test
    public void testCreateGame() throws Exception {
        int numberOfQuizzes = 5;

        String url = given().contentType(ContentType.JSON)
                .queryParam("n", numberOfQuizzes)
                .post()
                .then()
                .statusCode(201)
                .extract().header("Location");

        // Extract id from URL in Header Location
        Long id = Long.parseLong(url.substring(url.length() - 1, url.length()));

        // Get game by id and assert number of quizzes
        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("totalQuizzes", is(numberOfQuizzes));
    }

    @Test
    public void testCreateGameWithNegativeNumberOfQuizzesFails() throws Exception {
        int numberOfQuizzes = -2;

        get().then().statusCode(200).body("size()", is(0));

        given().contentType(ContentType.JSON)
                .queryParam("n", numberOfQuizzes)
                .post()
                .then()
                .statusCode(400);

        // Check that game was not created
        get().then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testGetRandomQuizzesStub() throws Exception {
        int numberOfQuizzes = 5;
        int category = 3;

        String quizJson = getJsonData("quizlist.json");
        String categoryJson = getJsonData("subsubcategories.json");
        stubJsonSubSubCategoryResponse(categoryJson);
        stubJsonQuizResponse(quizJson);

        given().contentType(ContentType.JSON)
                .baseUri("http://localhost")
                .port(8080)
                .basePath("/myquizgame/quiz/api/randomQuizzes")
                .queryParam("n", numberOfQuizzes)
                .queryParam("filter", category)
                .post()
                .then()
                .statusCode(200)
                .body("size()", is(numberOfQuizzes));
    }

    private void stubJsonQuizResponse(String json) throws Exception {
        wiremockServer.stubFor(
                WireMock.post(
                        urlPathEqualTo("/myquizgame/quiz/api/randomQuizzes"))
                        .withQueryParam("n", WireMock.containing("5"))
                        .withQueryParam("filter", WireMock.containing("3"))

                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withBody(json)));
    }

    private void stubJsonSubSubCategoryResponse(String json) throws Exception {
        wiremockServer.stubFor(
                WireMock.get(
                        urlPathEqualTo("/myquizgame/quiz/api/subsubcategories"))

                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json; charset=UTF-8")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withBody(json)));
    }

    private String getJsonData(String jsonFile) throws Exception {
        String data = "";
        try (Scanner in = new Scanner(new File(FILE_PATH + jsonFile))) {
            while (in.hasNextLine()) {
                data += in.nextLine() + "\n";
            }
        }
        return data;
    }
}
