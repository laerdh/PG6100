package no.westerdals.pg6100.rest.dto.converter;

import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.rest.dto.QuizDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuizConverter {

    private QuizConverter() {}

    public static QuizDto transform(Quiz entity) {
        Objects.requireNonNull(entity);

        QuizDto dto = new QuizDto();
        dto.id = entity.getId();
        dto.question = entity.getQuestion();
        dto.correctAnswer = entity.getCorrectAnswer();
        dto.answers =  entity.getAnswers();
        dto.parentCategoryId = entity.getParentSubSubCategory().getId();

        return dto;
    }

    public static List<QuizDto> transform(List<Quiz> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(QuizConverter::transform)
                .collect(Collectors.toList());
    }
}
