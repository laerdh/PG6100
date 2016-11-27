package no.westerdals.pg6100.quizapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("A quiz")
public class QuizDto {

    @ApiModelProperty("The id of the quiz")
    public Long id;

    @ApiModelProperty("The quiz question")
    public String question;

    @ApiModelProperty("List of answers")
    public List<String> answers;

    @ApiModelProperty("The parent category id")
    public Long parentCategoryId;


    public QuizDto() {}


    public QuizDto(Long id, String question, List<String> answers,
                   Long parentCategoryId) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.parentCategoryId = parentCategoryId;
    }
}
