package no.westerdals.pg6100.quizapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A category")
public class CategoryDto {

    @ApiModelProperty("The id of the category")
    public Long id;

    @ApiModelProperty("The name of the category")
    public String categoryName;


    public CategoryDto() {}


    public CategoryDto(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }
}
