package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.*;
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

    String ID_PARAM = "The id of the category";

    // GET

    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();


    @ApiOperation("Retrieve a category by its id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested category"),
            @ApiResponse(code = 404, message = "Category could not be found")
    })
    @Path("/{id}")
    @GET
    CategoryDto getCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of subcategories")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested list of subcategories"),
            @ApiResponse(code = 404, message = "Subcategories could not be found")
    })
    @Path("/{id}/subcategories")
    @GET
    List<SubCategoryDto> getSubCategories(
            @ApiParam(ID_PARAM)
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "The category was updated"),
            @ApiResponse(code = 404, message = "Category to be updated could not be found")
    })
    @Path("/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The category that will replace the old one. Id cannot be changed.")
            CategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of a category")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The category name was successfully updated"),
            @ApiResponse(code = 404, message = "Category to be updated could not be found")
    })
    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateCategoryName(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the category")
            String name
    );

    // DELETE

    @ApiOperation("Delete a category")
    @ApiResponse(code = 204, message = "The category was deleted")
    @DELETE
    @Path("/{id}")
    void deleteCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // DEPRECATED

    @ApiOperation("Deprectaded. Use \"/{id}\" instead. Retrieve a category by its id")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @GET
    @Deprecated
    Response getCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}/subcategories\" instead. Retrieve a list of sub-categories")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}/subcategories")
    @GET
    @Deprecated
    Response getSubCategoriesDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update an existing category")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Deprecated
    Response updateCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The category that will replace the old one. Id cannot be changed.")
                    CategoryDto dto
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update the name of a category")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response updateCategoryNameDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The new name of the category")
                    String name
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Delete a category")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deleteCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );
}