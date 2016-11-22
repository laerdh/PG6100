package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
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

    // GET

    @ApiOperation("Retrieve a list of all the subsubcategories")
    @GET
    List<SubSubCategoryDto> getSubSubCategories();


    @ApiOperation("Retrieve a subsubcategory")
    @Path("/id/{id}")
    @GET
    SubSubCategoryDto getSubSubCategory(
            @ApiParam("The id of the subsubcategory")
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of subsubcategories by parent id")
    @Path("/parent/{id}")
    @GET
    List<SubSubCategoryDto> getSubSubCategoriesByParentId(
            @ApiParam("The id of the parent subcategory")
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
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateSubSubCategory(
            @ApiParam("The id of the subsubcategory to be updated")
            @PathParam("id")
                    Long id,

            @ApiParam("The subsubcategory that will replace the old one. Id cannot be changed.")
                    SubSubCategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of a subsubcategory")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateSubSubCategoryName(
            @ApiParam("The id of the subsubcategory to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the subsubcategory")
            String name
    );

    // DELETE

    @ApiOperation("Delete a subsubcategory")
    @DELETE
    @Path("/id/{id}")
    void deleteSubSubCategory(
            @ApiParam("The id of the subsubcategory")
            @PathParam("id")
            Long id
    );
}
