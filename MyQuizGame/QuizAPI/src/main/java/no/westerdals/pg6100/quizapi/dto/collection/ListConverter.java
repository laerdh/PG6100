package no.westerdals.pg6100.quizapi.dto.collection;

import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.quizapi.dto.QuizDto;
import no.westerdals.pg6100.quizapi.dto.converter.QuizConverter;

import java.util.List;
import java.util.stream.Collectors;

public class ListConverter {

    // This creates a HAL DTO, but with the links (self, next, previous)
    // that still have to be set

    public static ListDto<QuizDto> transform(List<Quiz> quizList,
                                             int offset,
                                             int limit) {
        List<QuizDto> dtoList = null;

        if (quizList != null) {
            dtoList = quizList.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(QuizConverter::transform)
                    .collect(Collectors.toList());
        }

        ListDto<QuizDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = dtoList.size();

        return dto;
    }
}
