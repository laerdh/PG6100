package no.westerdals.pg6100.gameapi.resources;

import io.swagger.annotations.*;
import no.westerdals.pg6100.gameapi.GameApplication;
import no.westerdals.pg6100.gameapi.core.Game;
import no.westerdals.pg6100.gameapi.dao.GameDao;
import no.westerdals.pg6100.gameapi.utils.Formats;
import no.westerdals.pg6100.gameapi.utils.QuizApiUtil;
import org.assertj.core.util.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the game by given id"),
            @ApiResponse(code = 404, message = "Game could not be found")
    })
    @GET
    @Path("/{id}")
    public Game findById(@ApiParam("The id of game")
                         @PathParam("id")
                         Long id) {

        return gameDao.findById(id);
    }


    @ApiOperation("Add a game")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Created game with specified number of quizzes"),
            @ApiResponse(code = 404, message = "Not enough quizzes to start a game"),
            @ApiResponse(code = 400, message = "Number of quizzes must be 1 or more")
    })
    @POST
    public Response addGame(
            @DefaultValue("5")
            @ApiParam("The number of quizzes in game")
            @QueryParam("n")
            Integer n) {

        if (n < 1) {
            throw new WebApplicationException("Number of quizzes cannot be less than 1", 400);
        }

        // Call the other API
        // Find a subsubcategory with quiz
        String quizList = QuizApiUtil.getRandomQuizzes(n);
        if (Strings.isNullOrEmpty(quizList)) {
            throw new WebApplicationException("Something went wrong when collecting quizzes for this game", 500);
        }

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
}
