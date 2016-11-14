package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Question;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static no.westerdals.pg6100.backend.validation.InputValidation.formatInput;
import static no.westerdals.pg6100.backend.validation.InputValidation.validInput;

@Stateless
public class QuizEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CategoryEJB categoryEJB;


    public QuizEJB() {}


    public Long createQuestion(Long subSubCategoryId, String question,
                               List<String> answers, Integer correctAnswer) {
        if (!validInput(question) || subSubCategoryId == null || answers == null) {
            return null;
        }

        SubSubCategory subSubCategoryExist = categoryEJB.getSubSubCategoryById(subSubCategoryId);

        if (subSubCategoryExist == null || correctAnswer == null) {
            return null;
        }

        Question q = new Question();
        q.setParentSubSubCategory(subSubCategoryExist);
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);

        em.persist(q);
        subSubCategoryExist.getQuestions().add(q);

        return q.getId();
    }

    public Question getQuestion(Long id) {
        Query query = em.createNamedQuery(Question.GET_QUESTION_BY_ID);
        query.setParameter(1, id);

        return (Question) query.getSingleResult();
    }

    public List<Question> getAllQuestions() {
        Query query = em.createNamedQuery(Question.GET_ALL_QUESTIONS);

        return query.getResultList();
    }

    public boolean updateQuestion(Long questionId, Long subSubCategoryId, String question, List<String> answers, Integer correctAnswer) {
        if (!validInput(question) || subSubCategoryId == null || answers == null || correctAnswer == null) {
            return false;
        }

        Question q = em.find(Question.class, questionId);

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

    public int deleteQuestion(Long id) {
        // Must delete ElementCollection manually
        Question q = em.find(Question.class, id);

        if (q == null) {
            return 0;
        }

        q.getAnswers().clear();
        em.remove(q);

        return 1;
    }
}
