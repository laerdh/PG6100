package no.westerdals.pg6100.rest.api.implementation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.rest.api.QuizRestApi;
import no.westerdals.pg6100.rest.api.util.WebException;
import no.westerdals.pg6100.rest.dto.QuizDto;
import no.westerdals.pg6100.rest.dto.converter.QuizConverter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.json.JsonArray;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class QuizRest implements QuizRestApi {

    private static final String QUIZ_PATH = "/quizzes";

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
    public Response createQuiz(@ApiParam("Quiz id, question, answers and the id of the correct answer. Should not specify" + "id at the time of creation") QuizDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created quiz", 400);
        }

        if (!isValid(dto.question)) {
            throw new WebApplicationException("Must provide a question with answers", 400);
        }

        if (dto.question == null || dto.question.isEmpty()) {
            throw new WebApplicationException("Must provide a valid question", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide an id to parent category", 400);
        }

        Long id;
        try {
            id = quizEJB.createQuestion(dto.parentCategoryId, dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response.status(201)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    @Override
    public void deleteQuiz(@ApiParam("The id of the quiz") Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide an id", 400);
        }

        quizEJB.deleteQuestion(id);
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
