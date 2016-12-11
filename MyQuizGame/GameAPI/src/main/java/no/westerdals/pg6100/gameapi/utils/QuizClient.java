package no.westerdals.pg6100.gameapi.utils;

import com.google.gson.Gson;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class QuizClient extends HystrixCommand<String> {

    private static final int QUERY_LIMIT = 10;
    private Integer n;

    public QuizClient(Integer n) {
        super(HystrixCommandGroupKey.Factory.asKey("QuizzesApi"));
        this.n = n;
    }

    @Override
    protected String run() throws Exception {
        Response response;
        String quizList;
        int limit = 0;

        // Gather subsubcategories
        SubSubCategoryId[] categoryIds = getRandomSubSubCategories();

        do {
            int index = (int)(Math.random() * categoryIds.length);

            response = getQuizzes(limit, categoryIds[index].id.intValue());
            quizList = response.readEntity(String.class);
            limit++;

            if (limit == QUERY_LIMIT) {
                throw new WebApplicationException("Could not find subsubcategory with " + n + " quizzes", 404);
            }

        } while (response.getStatus() == 404);

        return quizList;
    }

    private Response getQuizzes(Integer limit, Integer category) {
        URI uri = UriBuilder.fromUri(QuizApiURI.RANDOM_QUIZZES_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();

        return client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);
    }

    private SubSubCategoryId[] getRandomSubSubCategories() {
        URI uri = UriBuilder.fromUri(QuizApiURI.SUBSUBCATEGORIES_PATH).build();

        Client client = ClientBuilder.newClient();
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();

        Gson gson = new Gson();

        return gson.fromJson(response.readEntity(String.class), SubSubCategoryId[].class);
    }

    private static class SubSubCategoryId{
        Long id;
    }
}
