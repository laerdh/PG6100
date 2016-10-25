package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.Question;
import no.westerdals.pg6100.backend.entity.SubCategory;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class QuizEJB {

    @PersistenceContext
    private EntityManager em;


    public QuizEJB() {}


    public boolean createCategory(String category) {
        if (!validInput(category)){
            return false;
        }

        Category exist = em.find(Category.class, formatInput(category));

        if (exist != null) {
            return false;
        }

        Category c = new Category();
        c.setCategory(formatInput(category));
        em.persist(c);
        return true;
    }

    public boolean createSubCategory(String parentCategory, String subCategory) {
        if (!validInput(parentCategory, subCategory)) {
            return false;
        }

        Category categoryExist = em.find(Category.class, formatInput(parentCategory));
        SubCategory subCategoryExist = em.find(SubCategory.class, formatInput(subCategory));

        if (categoryExist == null || subCategoryExist != null) {
            return false;
        }

        SubCategory c = new SubCategory();
        c.setSubCategory(formatInput(subCategory));
        c.setParentCategory(categoryExist);

        em.persist(c);
        categoryExist.getSubCategories().add(c);
        return true;
    }

    public boolean createSubSubCategory(String parentCategory, String subSubCategory) {
        if (!validInput(parentCategory, subSubCategory)) {
            return false;
        }

        SubCategory subCategoryExist = em.find(SubCategory.class, formatInput(parentCategory));
        SubSubCategory subSubCategoryExist = em.find(SubSubCategory.class, formatInput(subSubCategory));

        if (subCategoryExist == null || subSubCategoryExist != null) {
            return false;
        }

        SubSubCategory c = new SubSubCategory();
        c.setSubSubCategory(formatInput(subSubCategory));
        c.setParentCategory(subCategoryExist);


        em.persist(c);
        subCategoryExist.getSubSubCategories().add(c);
        return true;
    }

    public boolean addQuestion(String subSubCategory, String question, List<String> answers, String correctAnswer) {
        if (!validInput(subSubCategory, question, correctAnswer) || answers == null) {
            return false;
        }

        SubSubCategory subSubCategoryExist = em.find(SubSubCategory.class, formatInput(subSubCategory));

        if (subSubCategoryExist == null) {
            return false;
        }

        Question q = new Question();
        q.setParentCategory(subSubCategoryExist);
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);

        em.persist(q);
        subSubCategoryExist.getQuestions().add(q);
        return true;
    }

    public Question getQuestion(String category) {
        Query query = em.createQuery("select q from Question q where q.parentCategory = ?1 order by random()");
        query.setParameter(1, formatInput(category));
        query.setMaxResults(1);

        return (Question) query.getSingleResult();
    }

    private boolean validInput(String... input) {
        for(String s : input) {
            if (s == null || s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String formatInput(String input) {
        return input.toLowerCase();
    }
}
