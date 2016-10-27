package no.westerdals.pg6100.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A sub-sub-category")
public class SubSubCategoryDto {

    @ApiModelProperty("The id of the sub-sub-category")
    public String id;

    @ApiModelProperty("The name of the sub-sub-category")
    public String categoryName;

    @ApiModelProperty("The id of the parent sub-category")
    public String parentCategoryId;

    @ApiModelProperty("The name of the parent sub-category")
    public String parentCategoryName;

    public SubSubCategoryDto() {}

    public SubSubCategoryDto(String id, String categoryName,
                             String parentCategoryId, String parentCategoryName) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;

    }
}
