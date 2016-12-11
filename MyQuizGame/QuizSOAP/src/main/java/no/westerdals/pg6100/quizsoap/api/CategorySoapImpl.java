package no.westerdals.pg6100.quizsoap.api;

import com.google.common.base.Strings;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.quizsoap.dto.CategoryConverter;
import no.westerdals.pg6100.quizsoap.dto.CategoryDto;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import java.util.List;

@WebService(
        endpointInterface = "no.westerdals.pg6100.quizsoap.api.CategorySoapApi"
)
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CategorySoapImpl implements CategorySoapApi {

    @EJB
    private CategoryEJB categoryEJB;


    @Override
    public List<CategoryDto> getCategories() {
        return CategoryConverter.transform(categoryEJB.getCategories());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        if (id == null) {
            return null;
        }

        if (!categoryEJB.isCategoryPresent(id)) {
            return null;
        }

        return CategoryConverter.transform(categoryEJB.getCategory(id));
    }

    @Override
    public Long createCategory(CategoryDto dto) {
        if (dto.id != null) {
            return null;
        }

        if (Strings.isNullOrEmpty(dto.categoryName)) {
            return null;
        }

        Long id = categoryEJB.createCategory(dto.categoryName);

        return id;
    }

    @Override
    public void updateCategory(Long id, CategoryDto dto) {
        categoryEJB.updateCategory(id, dto.categoryName);
    }

    @Override
    public void updateCategoryName(Long id, String categoryName) {
        categoryEJB.updateCategory(id, categoryName);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryEJB.deleteCategory(id);
    }
}
