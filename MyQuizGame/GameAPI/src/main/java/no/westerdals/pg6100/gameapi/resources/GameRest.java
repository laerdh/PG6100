package no.westerdals.pg6100.gameapi.resources;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.gameapi.GameApplication;
import no.westerdals.pg6100.gameapi.core.Game;
import no.westerdals.pg6100.gameapi.dao.GameDao;
import no.westerdals.pg6100.gameapi.utils.Formats;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

@Api(value = "/games", description = "API for Quiz Games")
@Path("/games")
@Consumes({
        Formats.V1_JSON,
        Formats.BASE_JSON
})
@Produces({
        Formats.V1_JSON,
        Formats.BASE_JSON
})
public class GameRest {

    private final String RESOURCE_PATH = GameApplication.API_PATH + "/games";

    private GameDao gameDao;


    public GameRest(GameDao gameDao) {
        this.gameDao = gameDao;
    }


    @ApiOperation("Get all active games")
    @GET
    public List<Game> getAll(@DefaultValue("5")
                             @ApiParam("Number of games")
                             @QueryParam("n")
                             Integer n) {

        return gameDao.getAll(n);
    }


    @ApiOperation("Get an active game by id")
    @GET
    @Path("/{id}")
    public Game findById(@ApiParam("The id of game")
                         @PathParam("id")
                         Long id) {

        return gameDao.findById(id);
    }


    @ApiOperation("Add a game")
    @POST
    public Response addGame(
            @DefaultValue("5")
            @ApiParam("The number of quizzes in game")
            @QueryParam("n")
            Integer n,

            @ApiParam("Body with quizzes, answered quizzes and total quizzes")
            Game game) {

        if (n < 1) {
            throw new WebApplicationException("Number of quizzes cannot be less than 1", 400);
        }

        // Call the other API
        Response response = getRandomQuizzes(n, 3);
        String quizList = response.readEntity(String.class);

        // Hack: Save list of quizzes as JSON String
        String quizzes = "{ \"quizzes\" : " + quizList + " }";
        System.out.println("\n\nQUIZLIST: " + quizList + "\n\n");

        Long id;
        try {
            id = gameDao.insert(quizzes, 0, 0);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201)
                .location(URI.create(RESOURCE_PATH + "/" + id))
                .build();
    }

    private Response getRandomQuizzes(Integer limit, Integer category) {
        URI uri = UriBuilder.fromUri(GameApplication.QUIZ_RESOURCE_PATH)
                .queryParam("n", limit)
                .queryParam("filter", category)
                .build();

        Client client = ClientBuilder.newClient();

        return client.target(uri).request(MediaType.APPLICATION_JSON_TYPE).post(null);
    }


}
