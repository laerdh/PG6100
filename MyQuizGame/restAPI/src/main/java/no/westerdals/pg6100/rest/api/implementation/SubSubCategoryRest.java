package no.westerdals.pg6100.rest.api.implementation;

import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.rest.api.SubSubCategoryRestApi;
import no.westerdals.pg6100.rest.api.util.WebException;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;
import no.westerdals.pg6100.rest.dto.converter.SubSubCategoryConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubSubCategoryRest implements SubSubCategoryRestApi {

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public List<SubSubCategoryDto> getSubSubCategories() {
        return SubSubCategoryConverter.transform(categoryEJB.getAllSubSubCategories());
    }

    @Override
    public SubSubCategoryDto getSubSubCategory(@ApiParam("The id of the subsubcategory") Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoryById(id));
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategoriesByParentId(@ApiParam("The id of the parent subcategory") Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoriesByParentID(id));
    }

    // POST

    @Override
    public Long createSubSubCategory(@ApiParam("The id of the parent subsubcategory and category, name and id. " + "Should not specify id at time of creation") SubSubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for newly created subsubcategory", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must specify parent category id", 400);
        }

        if (dto.categoryName == null ||dto.categoryName.isEmpty()) {
            throw new WebApplicationException("Must specify a category name", 400);
        }

        Long id;
        Long parentId = Long.parseLong(dto.parentCategoryId);
        try {
            id = categoryEJB.createSubSubCategory(parentId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return id;
    }

    // DELETE

    @Override
    public void deleteSubSubCategory(@ApiParam("The id of the subsubcategory") Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        categoryEJB.deleteSubSubCategory(id);
    }
}
