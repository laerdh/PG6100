package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import no.westerdals.pg6100.rest.api.utils.Formats;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Api(value = "/randomQuiz", description = "API for getting random quiz")
@Path("/randomQuiz")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface RandomQuizRestApi {

    @ApiOperation("Get random quizzes. Can specify a category, subcategory or subsubcategory with optional filter")
    @ApiResponse(code = 307, message = "Temporary redirects to /quizzes/{id}")
    @GET
    Response getRandomQuiz(
            @ApiParam("Specify an id for category, subcategory og subsubcategory")
            @QueryParam("filter")
            Long n
    );
}
