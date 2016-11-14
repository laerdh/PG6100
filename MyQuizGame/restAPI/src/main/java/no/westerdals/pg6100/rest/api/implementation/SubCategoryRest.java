package no.westerdals.pg6100.rest.api.implementation;

import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.rest.api.SubCategoryRestApi;
import no.westerdals.pg6100.rest.api.util.WebException;
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
    public SubCategoryDto getSubCategoryById(@ApiParam("The id of the subcategory") Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoryById(id));
    }

    @Override
    public List<SubCategoryDto> getSubCategoriesByParentId(@ApiParam("The id of the category") Long id) {
        return SubCategoryConverter.transform(categoryEJB.getSubCategoriesByParentId(id));
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategories(@ApiParam("The id of the subcategory") Long id) {
        return SubSubCategoryConverter.transform(categoryEJB.getSubSubCategoriesByParentID(id));
    }

    // POST

    @Override
    public Response createSubCategory(@ApiParam("Id, name and category id. Should not specify id at time of creation") SubCategoryDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for newly created subcategory", 400);
        }

        Long id;
        try {
            id = categoryEJB.createSubCategory(dto.parentCategoryId, dto.categoryName);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response.status(201)
                .location(URI.create(SUBCATEGORY_PATH + "/" + id))
                .build();
    }

    // DELETE

    @Override
    public void deleteSubCategory(@ApiParam("The id of the subcategory") Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        categoryEJB.deleteSubCategory(id);
    }
}
