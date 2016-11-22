package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.rest.api.utils.Formats;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = "/categories", description = "API for Quiz Categories")
@Path("/categories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface CategoryRestApi {

    // GET

    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();


    @ApiOperation("Retrieve a category by its id")
    @Path("/id/{id}")
    @GET
    CategoryDto getCategory(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of sub-categories")
    @Path("/id/{id}/subcategories")
    @GET
    List<SubCategoryDto> getSubCategories(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of all categories that have at least one subcategory with at least one subsubcategory" +
            "with at least one quiz")
    @Path("/withQuizzes")
    @GET
    List<CategoryDto> getCategoriesWithQuizzes();


    @ApiOperation("Retrieve a list of all subsubcategories with at least one quiz")
    @Path("/withQuizzes/subsubcategories")
    @GET
    List<SubSubCategoryDto> getSubSubCategoriesWithQuizzes();

    // POST

    @ApiOperation("Create a category")
    @POST
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created category")
    Response createCategory(
            @ApiParam("The name of the category and id. Should not specify id at the time of creation")
            CategoryDto dto
    );

    // PUT

    @ApiOperation("Update an existing category")
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateCategory(
            @ApiParam("The id of the category")
            @PathParam("id")
                    Long id,

            @ApiParam("The category that will replace the old one. Id cannot be changed.")
                    CategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of a category")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @ApiResponse(code = 200, message = "The id of the updated category")
    Response updateCategoryName(
            @ApiParam("The id of the category to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the category")
            String name
    );

    // DELETE

    @ApiOperation("Delete a category")
    @DELETE
    @Path("/id/{id}")
    void deleteCategory(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id);
}