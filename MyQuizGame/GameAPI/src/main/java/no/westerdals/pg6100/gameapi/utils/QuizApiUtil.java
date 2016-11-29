package no.westerdals.pg6100.gameapi.utils;

import com.google.gson.Gson;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

public class QuizApiUtil {

    private static final String QUIZ_API_PATH = "http://localhost:8080/myquizgame/quiz/api";
    private static final String SUBSUBCATEGORIES_PATH = "/subsubcategories";
    private static final String RANDOM_QUIZZES_PATH = "/randomQuizzes";
    private static final int QUERY_LIMIT = 10;

    /*
     * GET all subsubcategories, then select one randomly.
     * If it doesn't contain enough quizzes, try another
     * one until QUERY_LIMIT is reached. Then throw 404.
     */

    public static String getRandomQuizzes(Integer n) {
        Response response;
        String quizList;
        int limit = 0;

        do {
            response = getQuizzes(n, getRandomSubSubcategories());
            quizList = response.readEntity(String.class);
            limit++;

            if (limit == QUERY_LIMIT) {
                throw new WebApplicationException("Could not find any subsubcategories with " + n + " quizzes", 404);
            }
        } while (response.getStatus() == 404);

        return quizList;
    }

    private static Response getQuizzes(Integer limit, Integer category) {
        URI uri = UriBuilder.fromUri(QUIZ_API_PATH + RANDOM_QUIZZES_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();

        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);
        checkIfError(response.getStatusInfo());

        return response;
    }

    private static int getRandomSubSubcategories() {
        URI uri = UriBuilder.fromUri(QUIZ_API_PATH + SUBSUBCATEGORIES_PATH).build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();
        checkIfError(response.getStatusInfo());

        Gson gson = new Gson();
        Wrapper[] arr = gson.fromJson(response.readEntity(String.class), Wrapper[].class);

        return (int)(Math.random() * arr.length);
    }

    private static void checkIfError(Response.StatusType status) {

        if (status.getFamily().equals(Response.Status.Family.CLIENT_ERROR)) {
            throw new ClientErrorException(status.getStatusCode());
        }
        if (status.getFamily().equals(Response.Status.Family.SERVER_ERROR)) {
            throw new ServerErrorException(status.getStatusCode());
        }
    }

    private static class Wrapper{
        Long id;
    }
}
