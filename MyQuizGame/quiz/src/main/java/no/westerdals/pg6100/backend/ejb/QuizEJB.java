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
        c.setCategoryName(formatInput(category));
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
        c.setSubCategoryName(formatInput(subCategory));
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
        c.setSubSubCategoryName(formatInput(subSubCategory));
        c.setParentSubCategory(subCategoryExist);


        em.persist(c);
        subCategoryExist.getSubSubCategories().add(c);
        return true;
    }

    public boolean createQuestion(String subSubCategory, String question, List<String> answers, String correctAnswer) {
        if (!validInput(subSubCategory, question, correctAnswer) || answers == null) {
            return false;
        }

        SubSubCategory subSubCategoryExist = em.find(SubSubCategory.class, formatInput(subSubCategory));

        if (subSubCategoryExist == null) {
            return false;
        }

        Question q = new Question();
        q.setParentSubSubCategory(subSubCategoryExist);
        q.setQuestion(question);
        q.setAnswers(answers);
        q.setCorrectAnswer(correctAnswer);

        em.persist(q);
        subSubCategoryExist.getQuestions().add(q);
        return true;
    }

    public List<Category> getCategories() {
        Query query = em.createNamedQuery(Category.GET_CATEGORIES);

        return query.getResultList();
    }

    public List<SubCategory> getSubCategories(String category) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORIES);
        query.setParameter(1, formatInput(category));

        return query.getResultList();
    }

    public List<SubCategory> getAllSubCategories() {
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES);

        return query.getResultList();
    }

    public List<SubSubCategory> getSubSubCategories(String subCategory) {
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORIES);
        query.setParameter(1, formatInput(subCategory));

        return query.getResultList();
    }

    public List<SubSubCategory> getAllSubSubCategories() {
        Query query = em.createNamedQuery(SubSubCategory.GET_ALL_SUBSUBCATEGORIES);

        return query.getResultList();
    }

    public Question getQuestion(String subSubCategory) {
        Query query = em.createQuery("select q from Question q where q.parentCategory = ?1 order by random()");
        query.setParameter(1, formatInput(subSubCategory));
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
