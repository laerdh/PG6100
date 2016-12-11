package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static no.westerdals.pg6100.backend.validation.InputValidation.validInput;

@Stateless
public class QuizEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CategoryEJB categoryEJB;


    public QuizEJB() {}


    public Long createQuiz(Long subSubCategoryId, String question,
                               List<String> answers, Integer correctAnswer) {
        if (!validInput(question) || subSubCategoryId == null || answers == null) {
            return null;
        }

        SubSubCategory subSubCategoryExist = categoryEJB.getSubSubCategoryById(subSubCategoryId);

        if (subSubCategoryExist == null || correctAnswer == null) {
            return null;
        }

        Quiz q = new Quiz();
        q.setParentSubSubCategory(subSubCategoryExist);
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);

        em.persist(q);
        subSubCategoryExist.getQuiz().add(q);

        return q.getId();
    }

    public Quiz getQuiz(Long id) {
        Query query = em.createNamedQuery(Quiz.GET_QUIZ_BY_ID);
        query.setParameter(1, id);

        return (Quiz) query.getSingleResult();
    }

    public boolean isPresent(Long id) {
        return em.find(Quiz.class, id) != null;
    }

    public List<Quiz> getAllQuizzes(int limit) {
        Query query = em.createNamedQuery(Quiz.GET_ALL_QUIZZES);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    public boolean updateQuiz(Long questionId, Long subSubCategoryId, String question, List<String> answers, Integer correctAnswer) {
        if (!validInput(question) || subSubCategoryId == null || answers == null || correctAnswer == null) {
            return false;
        }

        Quiz q = em.find(Quiz.class, questionId);

        if (q == null) {
            return false;
        }

        SubSubCategory subSubCategoryExist = em.find(SubSubCategory.class, subSubCategoryId);
        if (subSubCategoryExist == null) {
            return false;
        }

        q.setParentSubSubCategory(subSubCategoryExist);
        q.setQuestion(question);
        q.getAnswers().clear();
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);

        em.merge(q);
        return true;
    }

    public int deleteQuiz(Long id) {
        // Must delete ElementCollection manually
        Quiz q = em.find(Quiz.class, id);

        if (q == null) {
            return 0;
        }

        q.getAnswers().clear();
        em.remove(q);

        return 1;
    }
}
