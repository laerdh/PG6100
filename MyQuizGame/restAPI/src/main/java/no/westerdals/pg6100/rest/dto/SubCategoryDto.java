package no.westerdals.pg6100.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub-category")
public class SubCategoryDto {

    @ApiModelProperty("The id of the sub-category")
    public Long id;

    @ApiModelProperty("The name of the sub-category")
    public String categoryName;

    @ApiModelProperty("The id of the parent category")
    public Long parentCategoryId;


    public SubCategoryDto() {}


    public SubCategoryDto(Long id, String categoryName, Long parentCategoryId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
    }
}
