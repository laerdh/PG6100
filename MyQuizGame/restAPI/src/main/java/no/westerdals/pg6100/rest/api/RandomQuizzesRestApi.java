package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.*;
import no.westerdals.pg6100.rest.api.utils.Formats;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "/randomQuizzes", description = "API for getting a list of random quizzes")
@Path("/randomQuizzes")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface RandomQuizzesRestApi {

    @ApiOperation("Returns a list of random ids for quizzes based on the optional filter. No duplicates.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "A list of random id for quizzes"),
            @ApiResponse(code = 404, message = "Not enough quizzes to generate list")
    })
    @POST
    Response getRandomQuizzes(
            @ApiParam("Specify size of list to be returned")
            @DefaultValue("5")@QueryParam("n")
            Integer n,

            @ApiParam("Specify an id for a category, subcategory or subsubcategory")
            @QueryParam("filter")
            Long id
    );
}
