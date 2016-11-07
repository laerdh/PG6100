package no.westerdals.pg6100.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A quiz")
public class QuizDto {

    @ApiModelProperty("The id of the quiz")
    public String id;

    @ApiModelProperty("The quiz question")
    public String question;

    @ApiModelProperty("The id of the correct answer")
    public String correctAnswer;

    @ApiModelProperty("List of answers")
    public String answers;

    @ApiModelProperty("The parent category id")
    public String parentCategoryId;

    @ApiModelProperty("The parent category name")
    public String parentCategoryName;


    public QuizDto() {}


    public QuizDto(String id, String question, String correctAnswer, String answers,
                   String parentCategoryId, String parentCategoryName) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
    }
}
