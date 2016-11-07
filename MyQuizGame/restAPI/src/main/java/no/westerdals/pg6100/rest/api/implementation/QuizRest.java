package no.westerdals.pg6100.rest.api.implementation;

import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.rest.api.QuizRestApi;
import no.westerdals.pg6100.rest.dto.QuizDto;
import no.westerdals.pg6100.rest.dto.converter.QuizConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuizRest implements QuizRestApi {

    @EJB
    private QuizEJB quizEJB;

    @Override
    public List<QuizDto> getQuizzes() {
        return QuizConverter.transform(quizEJB.getAllQuestions());
    }

    @Override
    public QuizDto getQuiz(@ApiParam("The id of the quiz") Long id) {
        return QuizConverter.transform(quizEJB.getQuestion(id));
    }

    @Override
    public Long createQuiz(@ApiParam("Quiz id, question, answers and the id of the correct answer. Should not specify" + "id at the time of creation") QuizDto dto) {
        // TODO
        return null;
    }

    @Override
    public void deleteQuiz(@ApiParam("The id of the quiz") Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        quizEJB.deleteQuestion(id);
    }
}
