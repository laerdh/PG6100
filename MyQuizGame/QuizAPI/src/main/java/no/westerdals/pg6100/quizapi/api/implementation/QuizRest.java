package no.westerdals.pg6100.quizapi.api.implementation;

import com.google.common.base.Strings;
import no.westerdals.pg6100.backend.ejb.CategoryEJB;
import no.westerdals.pg6100.backend.ejb.QuizEJB;
import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.quizapi.api.QuizRestApi;
import no.westerdals.pg6100.quizapi.api.utils.WebException;
import no.westerdals.pg6100.quizapi.dto.QuizDto;
import no.westerdals.pg6100.quizapi.dto.QuizPostDto;
import no.westerdals.pg6100.quizapi.dto.converter.QuizConverter;

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

    @EJB
    private CategoryEJB categoryEJB;

    // GET

    @Override
    public List<QuizDto> getQuizzes() {
        return QuizConverter.transform(quizEJB.getAllQuizzes());
    }

    @Override
    public QuizDto getQuiz(Long id) {
        return QuizConverter.transform(quizEJB.getQuiz(id));
    }

    @Override
    public Response getCorrectAnswer(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!quizEJB.isPresent(id)) {
            throw new WebApplicationException("Cannot find quiz with id: " + id, 404);
        }

        Integer answer = null;
        try {
            answer = quizEJB.getQuiz(id).getCorrectAnswer();
        } catch (Exception e) {
            WebException.wrapException(e);
        }

        return Response.status(200)
                .entity(answer)
                .build();
    }

    // POST

    @Override
    public Response createQuiz(QuizPostDto dto) {
        if (dto.id != null) {
            throw new WebApplicationException("Cannot specify id for a newly created quiz", 400);
        }

        if (dto.parentCategoryId == null) {
            throw new WebApplicationException("Must provide an id to parent category", 400);
        }

        if (!categoryEJB.isSubSubCategoryPresent(dto.parentCategoryId)) {
            throw new WebApplicationException("Cannot find subsubcategory with id: " + dto.parentCategoryId, 404);
        }

        if (Strings.isNullOrEmpty(dto.question)) {
            throw new WebApplicationException("Must provide a valid question", 400);
        }

        Long id;
        try {
            id = quizEJB.createQuiz(dto.parentCategoryId, dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(201)
                .entity(id)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    // PUT

    @Override
    public Response updateQuiz(Long id, QuizPostDto dto) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (id.longValue() != dto.id) {
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (!quizEJB.isPresent(id)) {
            throw new WebApplicationException("Not allowed to create a quiz with PUT, and cannot find quiz with id: " +
                    id, 404);
        }

        try {
            quizEJB.updateQuiz(id, dto.parentCategoryId, dto.question, dto.answers, dto.correctAnswer);
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // PATCH

    @Override
    public Response updateQuizQuestion(Long id, String question) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!quizEJB.isPresent(id)) {
            throw new WebApplicationException("Cannot find quiz with id: " + id, 404);
        }

        if (Strings.isNullOrEmpty(question)) {
            throw new WebApplicationException("Question cannot be empty", 400);
        }

        Quiz q = quizEJB.getQuiz(id);

        try {
            quizEJB.updateQuiz(q.getId(), q.getParentSubSubCategory().getId(), question,
                    q.getAnswers(), q.getCorrectAnswer());
        } catch (Exception e) {
            throw WebException.wrapException(e);
        }

        return Response
                .status(200)
                .build();
    }

    // DELETE

    @Override
    public void deleteQuiz(Long id) {
        if (id == null) {
            throw new WebApplicationException("Must provide a valid id", 400);
        }

        if (!quizEJB.isPresent(id)) {
            throw new WebApplicationException("Cannot find quiz with id: " + id, 404);
        }

        quizEJB.deleteQuiz(id);
    }

    // DEPRECATED

    @Override
    public Response getQuizDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    @Override
    public Response updateQuizDeprecated(Long id, QuizDto dto) {
        return Response
                .status(301)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    @Override
    public Response updateQuizQuestionDeprecated(Long id, String question) {
        return Response
                .status(301)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }

    @Override
    public Response deleteQuizDeprecated(Long id) {
        return Response
                .status(301)
                .location(URI.create(QUIZ_PATH + "/" + id))
                .build();
    }
}
