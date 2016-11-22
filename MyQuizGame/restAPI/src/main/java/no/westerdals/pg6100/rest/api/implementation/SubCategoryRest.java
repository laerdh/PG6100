package no.westerdals.pg6100.rest.api.implementation;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.rest.api.SubCategoryRestApi;
import no.westerdals.pg6100.rest.api.utils.WebException;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;
import no.westerdals.pg6100.rest.dto.converter.SubCategoryConverter;
import no.westerdals.pg6100.rest.dto.converter.SubSubCategoryConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubCategoryRest implements SubCategoryRestApi {

    private static final String SUBCATEGORY_PATH = "/subcategories";

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public List<SubCategoryDto> getSubCategories() {
        return SubCategoryConverter.transform(categoryEJB.getAllSubCategories());
    }

    @Override
    public SubCategoryDto getSubCategory(Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoryById(id));
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoriesByParentId(id));
    }

    // POST

    @Override
    public Response createSubCategory(SubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for newly created subcategory", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide id for parent category", 400);
        }

        if (!categoryEJB.isCategoryPresent(dto.parentCategoryId)) {
            throw new WebApplicationException("Cannot find parent category with id: " + dto.parentCategoryId, 404);
        }

        Long id;
        try {
            id = categoryEJB.createSubCategory(dto.parentCategoryId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response.status(201)
                .entity(id)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }

    // PUT

    @Override
    public Response updateSubCategory(Long id, SubCategoryDto dto) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (id.longValue() != dto.id) {
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (!categoryEJB.isSubCategoryPresent(id)) {
            throw new WebApplicationException("Not allowed to create a subcategory with PUT, and cannot find subcategory with id: " + id, 404);
        }

        try {
            categoryEJB.updateSubCategory(id, dto.parentCategoryId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // PATCH

    @Override
    public Response updateSubCategoryName(Long id, String name) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find subcategory with id: " + id, 404);
        }

        if (Strings.isNullOrEmpty(name)) {
            throw new WebApplicationException("Category name cannot be empty", 400);
        }

        try {
            categoryEJB.updateSubCategoryName(id, name);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // DELETE

    @Override
    public void deleteSubCategory(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find subcategory with id: " + id, 404);
        }

        categoryEJB.deleteSubCategory(id);
    }

    // DEPRECATED

    @Override
    public Response getSubCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response getSubSubCategoriesDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id + "/subsubcategories"))
                .build();
    }

    @Override
    public Response getSubCategoriesByParentIdDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create("/categories/" + id + SUBCATEGORY_PATH))
                .build();
    }

    @Override
    public Response updateSubCategoryDeprecated(Long id, SubCategoryDto dto) {
        return Response
                .status(301)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response updateSubCategoryNameDeprecated(Long id, String name) {
        return Response
                .status(301)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response deleteSubCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }
}
