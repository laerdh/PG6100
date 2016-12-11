package no.westerdals.pg6100.gameapi.utils;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class QuizAnswerClient extends HystrixCommand<Integer> {

    private Long id;

    public QuizAnswerClient(Long id) {
        super(HystrixCommandGroupKey.Factory.asKey("QuizAnswerApi"));
        this.id = id;
    }

    @Override
    protected Integer run() throws Exception {
        URI uri = UriBuilder.fromUri(QuizApiURI.QUIZZES_PATH + "/" + id + "/answer")
                .build();

        Client client = ClientBuilder.newClient();

        Response response = client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).get();

        return response.readEntity(Integer.class);
    }
}
