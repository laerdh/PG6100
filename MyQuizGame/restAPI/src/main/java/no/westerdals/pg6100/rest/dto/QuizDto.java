package no.westerdals.pg6100.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("A quiz")
public class QuizDto {

    @ApiModelProperty("The id of the quiz")
    public Long id;

    @ApiModelProperty("The quiz question")
    public String question;

    @ApiModelProperty("The id of the correct answer")
    public Integer correctAnswer;

    @ApiModelProperty("List of answers")
    public String answers;

    @ApiModelProperty("The parent category id")
    public Long parentCategoryId;


    public QuizDto() {}


    public QuizDto(Long id, String question, Integer correctAnswer, String answers,
                   Long parentCategoryId) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.parentCategoryId = parentCategoryId;
    }
}
