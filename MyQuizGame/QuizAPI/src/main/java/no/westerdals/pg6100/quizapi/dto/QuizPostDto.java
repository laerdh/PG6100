package no.westerdals.pg6100.quizapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("A quiz which includes the correct answer")
public class QuizPostDto extends QuizDto {

    @ApiModelProperty("The id of the correct answer")
    public Integer correctAnswer;


    public QuizPostDto() {}


    public QuizPostDto(Long id, String question, Integer correctAnswer, List<String> answers,
                            Long parentCategoryId) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
        this.parentCategoryId = parentCategoryId;
    }
}
