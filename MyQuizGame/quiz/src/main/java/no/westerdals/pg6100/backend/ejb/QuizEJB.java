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


    public Long createQuestion(String subSubCategory, String question,
                               List<String> answers, String correctAnswer) {
        if (!validInput(subSubCategory, question, correctAnswer) || answers == null) {
            return null;
        }

        SubSubCategory subSubCategoryExist = categoryEJB.getSubSubCategoryByName(formatInput(subSubCategory));

        if (subSubCategoryExist == null) {
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

    public Question getQuestion(String subSubCategory) {
        Query query = em.createNamedQuery(Question.GET_CATEGORY_QUESTIONS);
        query.setParameter(1, formatInput(subSubCategory));
        query.setMaxResults(1);

        return (Question) query.getSingleResult();
    }

    public List<Question> getAllQuestions() {
        Query query = em.createNamedQuery(Question.GET_ALL_QUESTIONS);

        return query.getResultList();
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
