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
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Api(value = "/games", description = "API for Quiz Games")
@Path("/games")
@Consumes({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
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
                             @ApiParam("Number of quizzes in game")
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
            @ApiParam("Body with quizzes, answered quizzes and total quizzes")
            Game game) {

        if (game.getQuizzes() == null) {
            throw new WebApplicationException("Must provide quizzes", 400);
        }

        if (game.getAnswered() == null) {
            game.setAnswered(0);
        }

        // Hack: Save list of quizzes as JSON String
        Gson gson = new Gson();
        String quizzes = "{ \"quizzes\" : " + gson.toJson(game.getQuizzes()) + " }";

        Long id;
        try {
            id = gameDao.insert(quizzes, game.getAnswered(), game.getQuizzes().size());
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201)
                .location(URI.create(RESOURCE_PATH + "/" + id))
                .build();
    }
}
