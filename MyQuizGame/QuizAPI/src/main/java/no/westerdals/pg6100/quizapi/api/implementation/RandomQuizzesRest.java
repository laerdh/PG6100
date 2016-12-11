package no.westerdals.pg6100.quizapi.api.implementation;

import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.quizapi.api.RandomQuizzesRestApi;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;
import java.util.*;

import static no.westerdals.pg6100.quizapi.api.utils.QuizUtil.filterQuizzes;
import static no.westerdals.pg6100.quizapi.api.utils.QuizUtil.getRandom;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RandomQuizzesRest implements RandomQuizzesRestApi{

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private CategoryEJB categoryEJB;

    @Override
    public Response getRandomQuizzes(Integer n, Long id) {
        List<Quiz> all = quizEJB.getAllQuizzes(1000);
        List<Quiz> filtered = new ArrayList<>();
        List<Long> quizIds;

        if (all == null || all.size() < n) {
            return Response.status(404).build();
        }

        // TODO
        // Too many if-elseif-else here
        // Needs refactoring
        if (id != null) {
            if (categoryEJB.isCategoryPresent(id)) {
                filtered.addAll(filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getParentCategory().getId().equals(id)));
            } else if (categoryEJB.isSubCategoryPresent(id)) {
                filtered.addAll(filterQuizzes(all, q -> q.getParentSubSubCategory().getParentSubCategory()
                        .getId().equals(id)));
            } else if (categoryEJB.isSubSubCategoryPresent(id)) {
                filtered.addAll(filterQuizzes(all, q -> q.getParentSubSubCategory().getId().equals(id)));
            } else {
                return Response.status(404).build();
            }

            if (filtered.size() < n) {
                return Response.status(404).build();
            }

            quizIds = generateRandomList(filtered, n);
        } else {
            quizIds = generateRandomList(all, n);
        }

        return Response
                .status(200)
                .entity(quizIds)
                .build();
    }

    private List<Long> generateRandomList(List<Quiz> all, Integer n) {
        List<Long> ids = new ArrayList<>();
        Set<Long> quizId = new HashSet<>();

        while (ids.size() < n) {
            Quiz q = all.get(getRandom(all.size()));

            if (!quizId.contains(q.getId())) {
                ids.add(q.getId());
                quizId.add(q.getId());
            }
        }

        return ids;
    }
}