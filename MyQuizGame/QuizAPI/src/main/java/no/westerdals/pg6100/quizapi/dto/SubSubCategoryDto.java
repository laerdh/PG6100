package no.westerdals.pg6100.quizapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub-sub-category")
public class SubSubCategoryDto {

    @ApiModelProperty("The id of the sub-sub-category")
    public Long id;

    @ApiModelProperty("The name of the sub-sub-category")
    public String categoryName;

    @ApiModelProperty("The id of the parent sub-category")
    public Long parentCategoryId;


    public SubSubCategoryDto() {}


    public SubSubCategoryDto(Long id, String categoryName, Long parentCategoryId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;

    }
}
