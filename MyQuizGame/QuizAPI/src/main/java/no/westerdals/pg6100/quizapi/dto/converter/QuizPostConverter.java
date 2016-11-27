package no.westerdals.pg6100.quizapi.dto.converter;

import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.quizapi.dto.QuizPostDto;

import java.util.Objects;

public class QuizPostConverter {

    private QuizPostConverter() {}

    public static QuizPostDto transform(Quiz entity) {
        Objects.requireNonNull(entity);

        QuizPostDto dto = new QuizPostDto();
        dto.id = entity.getId();
        dto.question = entity.getQuestion();
        dto.correctAnswer = entity.getCorrectAnswer();
        dto.answers = entity.getAnswers();
        dto.parentCategoryId = entity.getParentSubSubCategory().getId();

        return dto;
    }
}
