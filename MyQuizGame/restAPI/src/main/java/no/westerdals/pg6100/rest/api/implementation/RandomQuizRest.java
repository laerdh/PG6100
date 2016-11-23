package no.westerdals.pg6100.rest.api.implementation;

import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.rest.api.RandomQuizRestApi;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RandomQuizRest implements RandomQuizRestApi {

    private static final String QUIZ_PATH = "/quizzes";

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public Response getRandomQuiz(Long n) {

        List<Quiz> all = quizEJB.getAllQuizzes();
        Optional<Quiz> selected = Optional.empty();

        if (all == null || all.size() < 1) {
            return Response
                    .status(404)
                    .build();
        }

        if (n != null) {
            if (categoryEJB.isCategoryPresent(n)) {
                selected = filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getParentCategory().getId().equals(n));
            } else if (categoryEJB.isSubCategoryPresent(n)) {
                selected = filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getId().equals(n));
            } else if (categoryEJB.isSubSubCategoryPresent(n)) {
                selected = filterQuizzes(all, q -> q.getParentSubSubCategory().getId().equals(n));
            } else {
                return Response.status(404).build();
            }
        }

        if (!selected.isPresent()) {
            selected = Optional.of(all.get(getRandom(all.size())));
        }
        Long id = selected.get().getId();

        return Response
                .status(307)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    private Optional<Quiz> filterQuizzes(List<Quiz> list, Predicate<? super Quiz> predicate) {
        return list.stream()
                .findFirst()
                .filter(predicate);
    }

    private int getRandom(int limit) {
        return (int)(Math.random() * (limit+1));
    }
}
