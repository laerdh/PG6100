package no.westerdals.pg6100.quizapi.api.implementation;

import com.google.common.base.Strings;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.quizapi.api.SubSubCategoryRestApi;
import no.westerdals.pg6100.quizapi.api.utils.WebException;
import no.westerdals.pg6100.quizapi.dto.SubSubCategoryDto;
import no.westerdals.pg6100.quizapi.dto.converter.SubSubCategoryConverter;

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
public class SubSubCategoryRest implements SubSubCategoryRestApi {

    private static final String SUBSUBCATEGORY_PATH = "/subsubcategories";

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public List<SubSubCategoryDto> getSubSubCategories() {
        return SubSubCategoryConverter.transform(categoryEJB.getAllSubSubCategories());
    }

    @Override
    public SubSubCategoryDto getSubSubCategory(Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoryById(id));
    }

    // POST

    @Override
    public Response createSubSubCategory(SubSubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for newly created subsubcategory", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must specify parent category id", 400);
        }

        if (!categoryEJB.isSubCategoryPresent(dto.parentCategoryId)) {
            throw new WebApplicationException("Cannot find parent category with id: " + dto.parentCategoryId, 404);
        }

        if (Strings.isNullOrEmpty(dto.categoryName)) {
            throw new WebApplicationException("Must specify a category name", 400);
        }

        Long id;
        try {
            id = categoryEJB.createSubSubCategory(dto.parentCategoryId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response.status(201)
                .entity(id)
                .location(URI.create(SUBSUBCATEGORY_PATH + "/" + id))
                .build();
    }

    // PUT

    @Override
    public Response updateSubSubCategory(Long id, SubSubCategoryDto dto) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (id.longValue() != dto.id) {
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (!categoryEJB.isSubSubCategoryPresent(id)) {
            throw new WebApplicationException("Not allowed to create a subsubcategory with PUT, and cannot find subsubcategory" +
                    "with id: " + id, 404);
        }

        try {
            categoryEJB.updateSubSubCategory(id, dto.parentCategoryId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // PATCH

    @Override
    public Response updateSubSubCategoryName(Long id, String name) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isSubSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find subsubcategory with id: " + id, 404);
        }

        if (Strings.isNullOrEmpty(name)) {
            throw new WebApplicationException("Category name cannot be empty", 400);
        }

        try {
            categoryEJB.updateSubSubCategoryName(id, name);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // DELETE

    @Override
    public void deleteSubSubCategory(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isSubSubCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find subsubcategory with id: " + id, 404);
        }

        categoryEJB.deleteSubSubCategory(id);
    }

    // DEPRECATED

    @Override
    public Response getSubSubCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(SUBSUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response getSubSubCategoriesByParentIdDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create("/subcategories/" + id + SUBSUBCATEGORY_PATH))
                .build();
    }

    @Override
    public Response updateSubSubCategoryDeprecated(Long id, SubSubCategoryDto dto) {
        return Response
                .status(301)
                .location(URI.create(SUBSUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response updateSubSubCategoryNameDeprecated(Long id, String name) {
        return Response
                .status(301)
                .location(URI.create(SUBSUBCATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response deleteSubSubCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(SUBSUBCATEGORY_PATH + "/" + id))
                .build();
    }
}
