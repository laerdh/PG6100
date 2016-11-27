package no.westerdals.pg6100.quizapi.api.utils;

/*
 * Quiz utilities
 * Helper methods
 */

import no.westerdals.pg6100.backend.entity.Quiz;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QuizUtil {

    public static List<Quiz> filterQuizzes(List<Quiz> list, Predicate<Quiz> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static int getRandom(int limit) {
        return (int)(Math.random() * limit);
    }
}
