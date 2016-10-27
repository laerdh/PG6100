package no.westerdals.pg6100.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub-category")
public class SubCategoryDto {

    @ApiModelProperty("The id of the sub-category")
    public String id;

    @ApiModelProperty("The name of the sub-category")
    public String categoryName;

    @ApiModelProperty("The id of the parent category")
    public String parentCategoryId;

    @ApiModelProperty("The name of the parent category")
    public String parentCategoryName;

    public SubCategoryDto() {}

    public SubCategoryDto(String id, String categoryName,
                          String parentCategoryId, String parentCategoryName) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
    }
}
