package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subsubcategories", description = "API for Quiz SubSubCategories")
@Path("/subsubcategories")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created subsubcategory")
    Response createSubSubCategory(
            @ApiParam("The id of the parent subsubcategory and category, name and id. " +
                    "Should not specify id at time of creation")
            SubSubCategoryDto dto
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
