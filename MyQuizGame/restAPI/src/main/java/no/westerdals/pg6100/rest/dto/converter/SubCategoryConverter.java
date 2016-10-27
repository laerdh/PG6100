package no.westerdals.pg6100.rest.dto.converter;

import no.westerdals.pg6100.backend.entity.SubCategory;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubCategoryConverter {

    private SubCategoryConverter() {}

    public static SubCategoryDto transform(SubCategory entity) {
        Objects.requireNonNull(entity);

        SubCategoryDto dto = new SubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.categoryName = entity.getCategoryName();
        dto.parentCategoryId = String.valueOf(entity.getParentCategory().getId());
        dto.parentCategoryName = entity.getParentCategory().getCategoryName();

        return dto;
    }

    public static List<SubCategoryDto> transform(List<SubCategory> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(SubCategoryConverter::transform)
                .collect(Collectors.toList());
    }
}
