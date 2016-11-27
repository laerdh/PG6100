package no.westerdals.pg6100.quizapi.dto.converter;

import no.westerdals.pg6100.backend.entity.SubSubCategory;
import no.westerdals.pg6100.quizapi.dto.SubSubCategoryDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubSubCategoryConverter {

    private SubSubCategoryConverter() {}

    public static SubSubCategoryDto transform(SubSubCategory entity) {
        Objects.requireNonNull(entity);

        SubSubCategoryDto dto = new SubSubCategoryDto();
        dto.id = entity.getId();
        dto.categoryName = entity.getCategoryName();
        dto.parentCategoryId = entity.getParentSubCategory().getId();

        return dto;
    }

    public static List<SubSubCategoryDto> transform(List<SubSubCategory> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(SubSubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
