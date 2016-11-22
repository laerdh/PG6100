package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.rest.api.utils.Formats;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subsubcategories", description = "API for Quiz SubSubCategories")
@Path("/subsubcategories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface SubSubCategoryRestApi {

    String ID_PARAM = "The id of the subsubcategory";

    // GET

    @ApiOperation("Retrieve a list of all the subsubcategories")
    @GET
    List<SubSubCategoryDto> getSubSubCategories();


    @ApiOperation("Retrieve a subsubcategory")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The requested subsubcategory"),
            @ApiResponse(code = 404, message = "The subsubcategory could not be found")
    })
    @Path("/{id}")
    @GET
    SubSubCategoryDto getSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // POST

    @ApiOperation("Create a subsubcategory")
    @POST
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created subsubcategory")
    Response createSubSubCategory(
            @ApiParam("The id of the parent subsubcategory and category, name and id. " +
                    "Should not specify id at time of creation")
            SubSubCategoryDto dto
    );

    // PUT

    @ApiOperation("Update an existing subsubcategory")
    @ApiResponse(code = 200, message = "The subsubcategory was successfully updated")
    @PUT
    @Path("/{id}")
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The subsubcategory that will replace the old one. Id cannot be changed.")
            SubSubCategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of a subsubcategory")
    @ApiResponse(code = 200, message = "The name of the subsubcategory was successfully updated")
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateSubSubCategoryName(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the subsubcategory")
            String name
    );

    // DELETE

    @ApiOperation("Delete a subsubcategory")
    @ApiResponse(code = 204, message = "The subsubcategory was deleted")
    @DELETE
    @Path("/{id}")
    void deleteSubSubCategory(
            @ApiParam(ID_PARAM)
            @PathParam("id")
            Long id
    );

    // DEPRECATED

    @ApiOperation("Deprecated. Use \"/{id}\" instead. Retrieve a subsubcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @GET
    @Deprecated
    Response getSubSubCategoryDeprecated(
            @ApiParam("The id of the subsubcategory")
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/subcategories/{id}/subsubcategories\" instead. Retrieve a list of subsubcategories by parent id")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response getSubSubCategoriesByParentIdDeprecated(
            @ApiParam("The id of the parent subcategory")
            @PathParam("id")
                    Long id
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update an existing subsubcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    @Deprecated
    Response updateSubSubCategoryDeprecated(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory that will replace the old one. Id cannot be changed.")
                    SubSubCategoryDto dto
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Update the name of a subsubcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PATCH
    @Path("/id/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Deprecated
    Response updateSubSubCategoryNameDeprecated(
            @ApiParam("The id of the subsubcategory to be updated")
            @PathParam("id")
                    Long id,

            @ApiParam("The new name of the subsubcategory")
                    String name
    );


    @ApiOperation("Deprecated. Use \"/{id}\" instead. Delete a subsubcategory")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deleteSubSubCategoryDeprecated(
            @ApiParam("The id of the subsubcategory")
            @PathParam("id")
                    Long id
    );
}
