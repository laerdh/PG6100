package no.westerdals.pg6100.rest.dto.converter;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.rest.dto.CategoryDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CategoryConverter {

    private CategoryConverter() {}

    public static CategoryDto transform(Category entity) {
        Objects.requireNonNull(entity);

        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.categoryName = entity.getCategoryName();

        return dto;
    }

    public static List<CategoryDto> transform(List<Category> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(CategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
