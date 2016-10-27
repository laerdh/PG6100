package no.westerdals.pg6100.rest.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Api(value = "/categories", description = "API for Quiz Categories")
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public interface CategoryRestApi {

    @ApiOperation("Retrieve a list of all the categories")
    @GET
    List<CategoryDto> getCategories();


    @ApiOperation("Retrieve a category by its id")
    @Path("/{id}")
    @GET
    CategoryDto getCategory(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id);


    @ApiOperation("Retrieve a list of sub-categories")
    @Path("/{id}/subcategories")
    @GET
    List<SubCategoryDto> getSubCategories(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id);


    @ApiOperation("Retrieve a sub-category")
    @Path("/{category_id}/subcategories/{id}")
    @GET
    SubCategoryDto getSubCategoryById(
            @ApiParam("The id of the root category")
            @PathParam("category_id")
            Long categoryId,

            @ApiParam("The id of the sub-category")
            @PathParam("id")
            Long id);


    @ApiOperation("Retrieve a list of sub-sub-categories")
    @Path("/{category_id}/subcategories/{id}/subsubcategories")
    @GET
    List<SubSubCategoryDto> getSubSubCategories(
            @ApiParam("The id of the root category")
            @PathParam("category_id")
            Long categoryId,

            @ApiParam("The id of the sub-category")
            @PathParam("id")
            Long id);


    @ApiOperation("Retrieve a subsub-category")
    @Path("/{category_id}/subcategories/{subcategory_id}/subsubcategories/{id}")
    @GET
    SubSubCategoryDto getSubSubCategory(
            @ApiParam("The id of the root category")
            @PathParam("category_id")
            Long categoryId,

            @ApiParam("The id of the sub-category")
            @PathParam("subcategory_id")
            Long subCategoryId,

            @ApiParam("The id of the subsub-category")
            @PathParam("id")
            Long id);


    @ApiOperation("Create a category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created category")
    Long createCategory(
            @ApiParam("The name of the category and id. Should not specify id at the time of creation")
            CategoryDto dto);


    @ApiOperation("Create a subcategory")
    @POST
    @Path("/subcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created sub-category")
    Long createSubCategory(
            @ApiParam("Root category id, the sub-category's name and id. Should not specify id at the time of creation")
            SubCategoryDto dto);


    @ApiOperation("Create a subsub-category")
    @POST
    @Path("/subcategories/subsubcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of the newly created subsub-category")
    Long createSubSubCategory(
            @ApiParam("The id of the parent category (subcategory), the name of the subsub-category and id. " +
                    "Should not specify id at the time of creation.")
            SubSubCategoryDto dto);


    @ApiOperation("Delete a category")
    @DELETE
    @Path("/{id}")
    void deleteCategory(
            @ApiParam("The id of the category")
            @PathParam("id")
            Long id);


    @ApiOperation("Delete a subcategory")
    @DELETE
    @Path("/subcategories/{id}")
    void deleteSubCategory(
            @ApiParam("The id of the subcategory")
            @PathParam("id")
            Long id);


    @ApiOperation("Delete a subsub-category")
    @DELETE
    @Path("/subcategories/subsubcategories/{id}")
    void deleteSubSubCategory(
            @ApiParam("The id of the subsub-category")
            @PathParam("id")
            Long id);
}
