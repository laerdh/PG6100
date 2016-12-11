package no.westerdals.pg6100.quizsoap.api;


import no.westerdals.pg6100.quizsoap.dto.CategoryDto;

import javax.jws.WebService;
import java.util.List;

@WebService(name = "CategorySoap")
public interface CategorySoapApi {

    List<CategoryDto> getCategories();

    CategoryDto getCategory(Long id);

    Long createCategory(CategoryDto dto);

    void updateCategory(Long id, CategoryDto dto);

    void updateCategoryName(Long id, String categoryName);

    void deleteCategory(Long id);
}
