package no.westerdals.pg6100.gameapi.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.gameapi.core.Game;
import no.westerdals.pg6100.gameapi.dao.GameDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(value = "/games", description = "API for Quiz Games")
@Path("/games")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class GameRest {

    private GameDAO gameDao;


    public GameRest(GameDAO gameDao) {
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
            @ApiParam("Body with uizzes, answered quizzes and total quizzes")
            Game game) {
        if (game.getQuizzes() == null) {
            throw new WebApplicationException("Must provide quizzes", 400);
        }

        if (game.getAnswered() == null) {
            game.setAnswered(0);
        }


        try {
            gameDao.insert(game.getQuizzes(), game.getAnswered(), game.getTotalQuizzes());
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), 500);
        }

        return Response.status(201).build();
    }
}
