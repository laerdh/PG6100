package no.westerdals.pg6100.quizapi.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.quizapi.api.utils.Formats;
import no.westerdals.pg6100.quizapi.dto.QuizDto;
import no.westerdals.pg6100.quizapi.dto.QuizPostDto;

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

    String ID_PARAM = "The id of the quiz";

    // GET

    @ApiOperation("Retrieve a list of quizzes")
    @GET
    List<QuizDto> getQuizzes();


    @ApiOperation("Retrieve a quiz by its id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested quiz"),
            @ApiResponse(code = 404, message = "Quiz could not be found")
    })
    @Path("/{id}")
    @GET
    QuizDto getQuiz(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );


    @ApiOperation("Get the correct answer from a quiz by providing an id to it")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The index of the correct answer"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @GET
    @Path("/{id}/answer")
    Response getCorrectAnswer(
            @ApiParam(ID_PARAM)
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
            QuizPostDto dto
    );

    // PUT

    @ApiOperation("Update a quiz")
    @ApiResponse(code = 200, message = "The quiz was successfully updated")
    @PUT
    @Path("/{id}")
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateQuiz(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The quiz that will replace the old one. Id cannot be changed.")
            QuizPostDto dto
    );

    // PATCH

    @ApiOperation("Update the question of a quiz")
    @ApiResponse(code = 200, message = "The question of a quiz was successfully updated")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateQuizQuestion(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The updated question")
            String question
    );

    // DELETE

    @ApiOperation("Delete a quiz")
    @ApiResponse(code = 204, message = "The quiz was deleted")
    @DELETE
    @Path("/{id}")
    void deleteQuiz(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // DEPRECATED

    @ApiOperation("Deprecated. Use \"/{id}\" instead. Retrieve a quiz by its id")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response getQuizDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Delete a quiz")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deleteQuizDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update a quiz")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PUT
    @Path("/id/{id}")
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Deprecated
    Response updateQuizDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The quiz that will replace the old one. Id cannot be changed.")
                    QuizDto dto
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update the question of a quiz")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response updateQuizQuestionDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The updated question")
                    String question
    );
}
