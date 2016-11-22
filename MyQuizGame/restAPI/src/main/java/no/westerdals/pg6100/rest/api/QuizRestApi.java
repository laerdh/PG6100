package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.rest.api.utils.Formats;
import no.westerdals.pg6100.rest.dto.QuizDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/quizzes", description = "API for Quizzes")
@Path("/quizzes")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface QuizRestApi {

    // GET

    @ApiOperation("Retrieve a list of quizzes")
    @GET
    List<QuizDto> getQuizzes();


    @ApiOperation("Retrieve a quiz by its id")
    @Path("/id/{id}")
    @GET
    QuizDto getQuiz(
            @ApiParam("The id of the quiz")
            @PathParam("id")
            Long id
    );

    // POST

    @ApiOperation("Create a quiz")
    @POST
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created quiz")
    Response createQuiz(
            @ApiParam("Quiz id, question, answers and the id of the correct answer. Should not specify" +
                "id at the time of creation")
            QuizDto dto
    );

    // PUT

    @ApiOperation("Update a quiz")
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateQuiz(
            @ApiParam("The id of the quiz to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The quiz that will replace the old one. Id cannot be changed.")
            QuizDto dto
    );

    // PATCH

    @ApiOperation("Update the question of a quiz")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateQuizQuestion(
            @ApiParam("The id of the quiz to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The updated question")
            String question
    );

    // DELETE

    @ApiOperation("Delete a quiz")
    @DELETE
    @Path("/id/{id}")
    void deleteQuiz(
            @ApiParam("The id of the quiz")
            @PathParam("id")
            Long id
    );
}
