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
    private final String API_PARAM = "The id of the game";

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

        List<Game> games = gameDao.getAll(n);
        games.forEach(g -> g.setCurrentQuiz(getQuizPath(g.getQuizzes().get(g.getAnswered()))));

        return games;
    }


    @ApiOperation("Get an active game by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the game by given id"),
            @ApiResponse(code = 404, message = "Game could not be found")
    })
    @GET
    @Path("/{id}")
    public Game findById(@ApiParam(API_PARAM)
                         @PathParam("id")
                         Long id) {
        
            Game game = gameDao.findById(id);
            if (game != null) {
                game.setCurrentQuiz(getQuizPath(game.getQuizzes().get(game.getAnswered())));
            }

            return game;
    }


    @ApiOperation("Add a game")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Created game with specified number of quizzes"),
            @ApiResponse(code = 404, message = "Could not find subsubcategory with n quizzes"),
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
            id = gameDao.insert(quizzes, 0, n);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201)
                .location(URI.create(RESOURCE_PATH + "/" + id))
                .build();
    }


    @ApiOperation("Post an answer to a quiz")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Correct answer was given"),
            @ApiResponse(code = 204, message = "Wrong answer was given and the quiz ended")
    })
    @POST
    @Path("/{id}")
    public Response postAnswer(
            @ApiParam(API_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("Answer to quiz")
            @QueryParam("answer")
            Integer answer) {

        if (answer < 0) {
            throw new WebApplicationException("Not a valid answer", 400);
        }

        Game g = gameDao.findById(id);

        if (g == null) {
            return Response.status(404).build();
        }

        // TODO:
        // Condition to check if quiz is finished

        Long quizId = g.getQuizzes().get(g.getAnswered());
        Integer correctAnswer = QuizApiUtil.getQuizAnswer(quizId).readEntity(Integer.class);

        if (!answer.equals(correctAnswer)) {
            deleteGame(g.getId());
            return Response.status(204).build();
        }

        /*
         * Answer was correct. Increment
         * answered questions by 1 and
         * return 200 OK.
         */
        gameDao.updateAnswer(g.getId());
        return Response.status(200).build();
    }


    @ApiOperation("Delete a game")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Game was deleted"),
            @ApiResponse(code = 404, message = "Game could not be found")
    })
    @Path("/{id}")
    @DELETE
    public Response deleteGame(
            @ApiParam(API_PARAM)
            Long id) {

        if (gameDao.deleteGame(id) > 0) {
            return Response.status(204)
                    .build();
        }
        return Response.status(404)
                .build();
    }


    private String getQuizPath(Long id) {
        return URI.create(QuizApiUtil.QUIZ_API_PATH + QuizApiUtil.QUIZZES_PATH
                + "/" + id).toString();
    }
}
