package no.westerdals.pg6100.rest.dto.converter;

import no.westerdals.pg6100.backend.entity.Question;
import no.westerdals.pg6100.rest.dto.QuizDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuizConverter {

    private QuizConverter() {}

    public static QuizDto transform(Question entity) {
        Objects.requireNonNull(entity);

        QuizDto dto = new QuizDto();
        dto.id = String.valueOf(entity.getId());
        dto.question = entity.getQuestion();
        dto.correctAnswer = String.valueOf(entity.getCorrectAnswer());
        dto.answers =  entity.getAnswers().toString();
        dto.parentCategoryId = String.valueOf(entity.getParentSubSubCategory().getId());
        dto.parentCategoryName = entity.getParentSubSubCategory().getCategoryName();

        return dto;
    }

    public static List<QuizDto> transform(List<Question> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(QuizConverter::transform)
                .collect(Collectors.toList());
    }
}
