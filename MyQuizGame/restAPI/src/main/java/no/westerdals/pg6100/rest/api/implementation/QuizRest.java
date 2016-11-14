package no.westerdals.pg6100.rest.api.implementation;

import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.rest.api.QuizRestApi;
import no.westerdals.pg6100.rest.api.utils.WebException;
import no.westerdals.pg6100.rest.dto.QuizDto;
import no.westerdals.pg6100.rest.dto.converter.QuizConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuizRest implements QuizRestApi {

    private static final String QUIZ_PATH = "/quizzes";

    @EJB
    private QuizEJB quizEJB;

    // GET

    @Override
    public List<QuizDto> getQuizzes() {
        return QuizConverter.transform(quizEJB.getAllQuizzes());
    }

    @Override
    public QuizDto getQuiz(@ApiParam("The id of the quiz") Long id) {
        return QuizConverter.transform(quizEJB.getQuiz(id));
    }

    // PUT

    @Override
    public void updateQuiz(Long id, QuizDto dto) {
        if (id == null) {
            throw new WebApplicationException("Please provide a valid id", 400);
        }

        if (id.longValue() != dto.id) {
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (quizEJB.getQuiz(id) == null) {
            throw new WebApplicationException("Not allowed to create a quiz with PUT, and cannot find quiz with id: " +
                    id, 404);
        }

        try {
            quizEJB.updateQuiz(id, dto.parentCategoryId, dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }
    }

    // POST

    @Override
    public Long createQuiz(@ApiParam("Quiz id, question, answers and the id of the correct answer. Should not specify " +
            "id at the time of creation") QuizDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created quiz", 400);
        }

        if (!isValid(dto.question)) {
            throw new WebApplicationException("Must provide a valid question", 400);
        }


        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide an id to parent category", 400);
        }

        Long id;
        try {
            id = quizEJB.createQuiz(dto.parentCategoryId, dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return id;
    }

    // DELETE

    @Override
    public void deleteQuiz(@ApiParam("The id of the quiz") Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        quizEJB.deleteQuiz(id);
    }

    private boolean isValid(String... input) {
        for (String s : input) {
            if (s == null || s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
