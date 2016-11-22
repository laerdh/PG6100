package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;
import no.westerdals.pg6100.rest.api.utils.Formats;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.print.attribute.standard.Media;
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

    // GET

    @ApiOperation("Retrieve a list of subcategories")
    @GET
    List<SubCategoryDto> getSubCategories();


    @ApiOperation("Retrieve a subcategory")
    @Path("/id/{id}")
    @GET
    SubCategoryDto getSubCategoryById(
            @ApiParam("The id of the subcategory")
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve subcategories by providing id of parent")
    @Path("/parent/{id}")
    @GET
    List<SubCategoryDto> getSubCategoriesByParentId(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id
    );


    @ApiOperation("Retrieve a list of subsubcategories")
    @Path("/id/{id}/subsubcategories")
    @GET
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("The id of the subcategory")
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
    @Path("/id/{id}")
    @PUT
    @Consumes({Formats.BASE_JSON, Formats.V1_JSON})
    Response updateSubCategory(
            @ApiParam("The id of the subcategory to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The subcategory that will replace the old one. Id cannot be changed")
            SubCategoryDto dto
    );

    // PATCH

    @ApiOperation("Update the name of an existing subcategory")
    @Path("/id/{id}")
    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    Response updateSubCategoryName(
            @ApiParam("The id of the subcategory to be updated")
            @PathParam("id")
            Long id,

            @ApiParam("The new name of the subcategory")
            String name
    );

    // DELETE

    @ApiOperation("Delete a subcategory")
    @DELETE
    @Path("/id/{id}")
    void deleteSubCategory(
            @ApiParam("The id of the subcategory")
            @PathParam("id")
            Long id
    );

}
