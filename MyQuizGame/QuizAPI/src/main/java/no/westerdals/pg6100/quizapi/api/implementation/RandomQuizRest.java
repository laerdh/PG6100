package no.westerdals.pg6100.quizapi.api.implementation;

import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.quizapi.api.RandomQuizRestApi;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static no.westerdals.pg6100.quizapi.api.utils.QuizUtil.filterQuizzes;
import static no.westerdals.pg6100.quizapi.api.utils.QuizUtil.getRandom;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RandomQuizRest implements RandomQuizRestApi {

    private static final String QUIZ_PATH = "/quizzes";

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public Response getRandomQuiz(Long id) {
        List<Quiz> all = quizEJB.getAllQuizzes(1000);
        List<Quiz> filtered;
        Long quizId;

        if (all == null || all.isEmpty()) {
            return Response.status(404).build();
        }

        if (id != null) {
            if (categoryEJB.isCategoryPresent(id)) {
                filtered = filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getParentCategory().getId().equals(id));
            } else if (categoryEJB.isSubCategoryPresent(id)) {
                filtered = filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getId().equals(id));
            } else if (categoryEJB.isSubSubCategoryPresent(id)) {
                filtered = filterQuizzes(all, q -> q.getParentSubSubCategory().getId().equals(id));
            } else {
                return Response.status(404).build();
            }

            if (filtered.isEmpty()) {
                return Response.status(404).build();
            }
            quizId = filtered.get(getRandom(filtered.size())).getId();

        } else {
            quizId = all.get(getRandom(all.size())).getId();
        }

        return Response
                .status(307)
                .location(URI.create(QUIZ_PATH + "/" + quizId))
                .build();
    }
}
