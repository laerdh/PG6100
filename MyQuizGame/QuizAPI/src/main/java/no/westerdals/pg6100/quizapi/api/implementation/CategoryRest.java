package no.westerdals.pg6100.quizapi.api.implementation;

import com.google.common.base.Strings;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.quizapi.api.CategoryRestApi;
import no.westerdals.pg6100.quizapi.api.utils.WebException;
import no.westerdals.pg6100.quizapi.dto.CategoryDto;
import no.westerdals.pg6100.quizapi.dto.SubCategoryDto;
import no.westerdals.pg6100.quizapi.dto.SubSubCategoryDto;
import no.westerdals.pg6100.quizapi.dto.converter.CategoryConverter;
import no.westerdals.pg6100.quizapi.dto.converter.SubCategoryConverter;
import no.westerdals.pg6100.quizapi.dto.converter.SubSubCategoryConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategoryRest implements CategoryRestApi {

    private static final String CATEGORY_PATH = "/categories";

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public List<CategoryDto> getCategories() {
        return CategoryConverter.transform(categoryEJB.getCategories());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return CategoryConverter.transform(categoryEJB.getCategory(id));
    }

    @Override
    public List<CategoryDto> getCategoriesWithQuizzes() {
        return CategoryConverter.transform(categoryEJB.getAllCategoriesWithQuizzes());
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategoriesWithQuizzes() {
        return SubSubCategoryConverter.transform(categoryEJB.getAllSubSubCategoryWithQuizzes());
    }

    @Override
    public List<SubCategoryDto> getSubCategories(Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoriesByParentId(id));
    }

    // POST

    @Override
    public Response createCategory(CategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created category", 400);
        }

        Long id;
        try {
            id = categoryEJB.createCategory(dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(201)
                .entity(id)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }

    // PUT

    @Override
    public Response updateCategory(Long id, CategoryDto dto) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }
        if (id.longValue() != dto.id) {
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (!categoryEJB.isCategoryPresent(id)) {
            throw new WebApplicationException("Not allowed to create a category with PUT, and cannot find category with id: " + id, 404);
        }

        try {
            categoryEJB.updateCategory(id, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // PATCH

    @Override
    public Response updateCategoryName(Long id, String name) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find category with id: " + id, 404);
        }

        if (Strings.isNullOrEmpty(name)) {
            throw new WebApplicationException("Category name cannot be empty", 400);
        }

        try {
            categoryEJB.updateCategory(id, name);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // DELETE

    @Override
    public void deleteCategory(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!categoryEJB.isCategoryPresent(id)) {
            throw new WebApplicationException("Cannot find category with id: " + id, 404);
        }

        categoryEJB.deleteCategory(id);
    }

    // DEPRECATED

    @Override
    public Response getCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response getSubCategoriesDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(CATEGORY_PATH + "/" + id + "/subcategories"))
                .build();
    }

    @Override
    public Response updateCategoryDeprecated(Long id, CategoryDto dto) {
        return Response
                .status(301)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response updateCategoryNameDeprecated(Long id, String name) {
        return Response
                .status(301)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }

    @Override
    public Response deleteCategoryDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }
}
