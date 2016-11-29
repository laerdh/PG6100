package no.westerdals.pg6100.gameapi.utils;

import com.google.gson.Gson;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

public class QuizApiUtil {

    public static final String QUIZ_API_PATH = "http://localhost:8080/myquizgame/quiz/api";
    public static final String SUBSUBCATEGORIES_PATH = "/subsubcategories";
    public static final String RANDOM_QUIZZES_PATH = "/randomQuizzes";
    public static final String QUIZZES_PATH = "/quizzes";

    private static final int QUERY_LIMIT = 10;

    private static SubSubCategoryId[] categoryIds;

    /*
     * GET all subsubcategories, then select one randomly.
     * If it doesn't contain enough quizzes, try another
     * one until QUERY_LIMIT is reached. Then throw 404.
     */

    public static String getRandomQuizzes(Integer n) {
        Response response;
        String quizList;
        int limit = 0;

        // Gather subsubcategories
        getRandomSubSubcategories();

        do {
            int index = (int)(Math.random() * categoryIds.length);

            response = getQuizzes(n, categoryIds[index].id.intValue());
            quizList = response.readEntity(String.class);
            limit++;

            if (limit == QUERY_LIMIT) {
                throw new WebApplicationException("Could not find subsubcategory with " + n + " quizzes", 404);
            }
        } while (response.getStatus() == 404);

        return quizList;
    }

    public static Response getQuizAnswer(Long id) {
        URI uri = UriBuilder.fromUri(QUIZ_API_PATH + QUIZZES_PATH + "/" + id + "/answer")
                .build();

        Client client = ClientBuilder.newClient();

        return client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    private static Response getQuizzes(Integer limit, Integer category) {
        URI uri = UriBuilder.fromUri(QUIZ_API_PATH + RANDOM_QUIZZES_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();

        return client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);
    }

    private static void getRandomSubSubcategories() {
        URI uri = UriBuilder.fromUri(QUIZ_API_PATH + SUBSUBCATEGORIES_PATH).build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();

        Gson gson = new Gson();
        categoryIds = gson.fromJson(response.readEntity(String.class), SubSubCategoryId[].class);
    }

    private static class SubSubCategoryId{
        Long id;
    }
}
