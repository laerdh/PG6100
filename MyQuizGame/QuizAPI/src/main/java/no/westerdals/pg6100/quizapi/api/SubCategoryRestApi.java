package no.westerdals.pg6100.quizapi.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.quizapi.api.utils.Formats;
import no.westerdals.pg6100.quizapi.dto.SubCategoryDto;
import no.westerdals.pg6100.quizapi.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subcategories", description = "API for Quiz Subcategories")
@Path("/subcategories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface SubCategoryRestApi {

    String ID_PARAM = "The ID of the subcategory";

    // GET

    @ApiOperation("Retrieve a list of subcategories")
    @GET
    List<SubCategoryDto> getSubCategories();


    @ApiOperation("Retrieve a subcategory")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested subcategory"),
            @ApiResponse(code = 404, message = "Subcategory could not be found")
    })
    @Path("/{id}")
    @GET
    SubCategoryDto getSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of subsubcategories")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested list of subsubcategories"),
            @ApiResponse(code = 404, message = "Requested list of subsubcategories could not be found")
    })
    @GET
    @Path("/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // POST

    @ApiOperation("Create a subcategory")
    @POST
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created subcategory")
    Response createSubCategory(
            @ApiParam("Category id, name and id. Should not specify id at time of creation")
            SubCategoryDto dto
    );

    // PUT

    @ApiOperation("Update an existing subcategory")
    @ApiResponse(code = 200, message = "Subcategory was successfully updated")
    @PUT
    @Path("/{id}")
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The subcategory that will replace the old one. Id cannot be changed.")
            SubCategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of an existing subcategory")
    @ApiResponse(code = 200, message = "The name of the subcategory was successfully updated")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateSubCategoryName(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the subcategory")
            String name
    );

    // DELETE

    @ApiOperation("Delete a subcategory")
    @ApiResponse(code = 204, message = "The subcategory was deleted")
    @DELETE
    @Path("/{id}")
    void deleteSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // DEPRECATED

    @ApiOperation("Deprecated. Use \"/{id}\" instead. Retrieve a subcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @GET
    @Deprecated
    Response getSubCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}/subsubcategories\" instead. Retrieve a list of subsubcategories")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}/subsubcategories")
    @Deprecated
    Response getSubSubCategoriesDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/categories/{id}/subcategories\" instead. Retrieve subcategories by providing id of parent")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response getSubCategoriesByParentIdDeprecated(
            @ApiParam("The id of the parent category")
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update an existing subcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PUT
    @Path("/id/{id}")
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Deprecated
    Response updateSubCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The subcategory that will replace the old one. Id cannot be changed.")
                    SubCategoryDto dto
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update the name of an existing subcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response updateSubCategoryNameDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The new name of the subcategory")
                    String name
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Delete a subcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deleteSubCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id
    );
}
