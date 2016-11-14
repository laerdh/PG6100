package no.westerdals.pg6100.rest.api.implementation;

import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.rest.api.CategoryRestApi;
import no.westerdals.pg6100.rest.api.util.WebException;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;
import no.westerdals.pg6100.rest.dto.converter.CategoryConverter;
import no.westerdals.pg6100.rest.dto.converter.SubCategoryConverter;
import no.westerdals.pg6100.rest.dto.converter.SubSubCategoryConverter;

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

        return Response.status(201)
                .location(URI.create(CATEGORY_PATH + "/" + id))
                .build();
    }

    // DELETE

    @Override
    public void deleteCategory(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        categoryEJB.deleteCategory(id);
    }
}
