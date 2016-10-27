package no.westerdals.pg6100.rest.api;

import com.google.common.base.Throwables;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
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
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategoryRest implements CategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;


    @Override
    public List<CategoryDto> getCategories() {
        return CategoryConverter.transform(categoryEJB.getCategories());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return CategoryConverter.transform(categoryEJB.getCategory(id));
    }

    @Override
    public List<SubCategoryDto> getSubCategories(Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoriesByParentId(id));
    }

    @Override
    public SubCategoryDto getSubCategoryById(Long categoryId, Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoryByParentIdAndId(categoryId, id));
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(Long categoryId, Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoryByRootIdAndParentId(categoryId, id));
    }

    @Override
    public SubSubCategoryDto getSubSubCategory(Long categoryId, Long subCategoryId, Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoryByRootParentAndId(categoryId, subCategoryId, id));
    }

    @Override
    public Long createCategory(CategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created category", 400);
        }

        Long id;
        try {
            id = categoryEJB.createCategory(dto.categoryName);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public Long createSubCategory(SubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created subcategory", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide id to parent category", 400);
        }

        Long id;
        Long parentId = Long.parseLong(dto.parentCategoryId);
        try {
            id = categoryEJB.createSubCategory(parentId, dto.categoryName);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public Long createSubSubCategory(SubSubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created subsub-category", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide id to parent category", 400);
        }

        Long id;
        Long parentId = Long.parseLong(dto.parentCategoryId);
        try {
            id = categoryEJB.createSubSubCategory(parentId, dto.categoryName);
        } catch (Exception e) {
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryEJB.deleteCategory(id);
    }

    @Override
    public void deleteSubCategory(Long id) {
        categoryEJB.deleteSubCategory(id);
    }

    @Override
    public void deleteSubSubCategory(Long id) {
        categoryEJB.deleteSubSubCategory(id);
    }

    private WebApplicationException wrapException(Exception e) throws WebApplicationException {
        Throwable cause = Throwables.getRootCause(e);

        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
