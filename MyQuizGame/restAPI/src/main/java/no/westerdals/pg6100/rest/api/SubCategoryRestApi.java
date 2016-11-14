package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/subcategories", description = "API for Quiz Subcategories")
@Path("/subcategories")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 201, message = "The id of the newly created subcategory")
    Response createSubCategory(
            @ApiParam("Category id, name and id. Should not specify id at time of creation")
            SubCategoryDto dto
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
