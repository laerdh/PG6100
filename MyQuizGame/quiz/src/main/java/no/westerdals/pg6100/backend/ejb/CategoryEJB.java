package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.Quiz;
import no.westerdals.pg6100.backend.entity.SubCategory;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.*;

import static no.westerdals.pg6100.backend.validation.InputValidation.formatInput;
import static no.westerdals.pg6100.backend.validation.InputValidation.validInput;

@Stateless
public class CategoryEJB {

    private int MAX_FROM_DB = 1000;

    @PersistenceContext
    private EntityManager em;

    @EJB
    private QuizEJB quizEJB;


    public CategoryEJB() {}


    public Long createCategory(String category) {
        if (!validInput(category)){
            return null;
        }

        Category exist = getCategory(formatInput(category));

        if (exist != null) {
            return null;
        }

        Category c = new Category();
        c.setCategoryName(formatInput(category));
        em.persist(c);

        return c.getId();
    }

    public boolean updateCategory(Long id, String category) {
        if (id == null || !validInput(category)) {
            return false;
        }

        Category c = em.find(Category.class, id);

        if (c == null) {
            return false;
        }

        // Update attribute(s)
        c.setCategoryName(formatInput(category));

        em.merge(c);
        return true;
    }

    public Long createSubCategory(Long parentId, String subCategory) {
        if (!validInput(subCategory) || parentId == null) {
            return null;
        }

        Category categoryExist = em.find(Category.class, parentId);
        SubCategory subCategoryExist = getSubCategoryByName(formatInput(subCategory));

        if (categoryExist == null || subCategoryExist != null) {
            return null;
        }

        SubCategory c = new SubCategory();
        c.setCategoryName(formatInput(subCategory));
        c.setParentCategory(categoryExist);

        em.persist(c);
        categoryExist.getSubCategories().add(c);

        return c.getId();
    }

    public boolean updateSubCategory(Long id, Long parentId, String subCategory) {
        if (id == null || !validInput(subCategory)) {
            return false;
        }

        SubCategory sc = em.find(SubCategory.class, id);

        if (sc == null) {
            return false;
        }

        Category parentCategory = em.find(Category.class, parentId);

        if (parentCategory == null) {
            return false;
        }

        // Update attributes
        sc.setParentCategory(parentCategory);
        sc.setCategoryName(formatInput(subCategory));

        em.merge(sc);
        return true;
    }

    public boolean updateSubCategoryName(Long id, String subCategory) {
        if (id == null || !validInput(subCategory)) {
            return false;
        }

        SubCategory sc = em.find(SubCategory.class, id);

        if (sc == null) {
            return false;
        }

        sc.setCategoryName(formatInput(subCategory));

        em.merge(sc);
        return true;
    }

    public Long createSubSubCategory(Long parentId, String subSubCategory) {
        if (!validInput(subSubCategory) || parentId == null) {
            return null;
        }

        SubCategory subCategoryExist = em.find(SubCategory.class, parentId);
        SubSubCategory subSubCategoryExist = getSubSubCategoryByName(formatInput(subSubCategory));

        if (subCategoryExist == null || subSubCategoryExist != null) {
            return null;
        }

        SubSubCategory c = new SubSubCategory();
        c.setCategoryName(formatInput(subSubCategory));
        c.setParentSubCategory(subCategoryExist);


        em.persist(c);
        subCategoryExist.getSubSubCategories().add(c);

        return c.getId();
    }

    public boolean updateSubSubCategory(Long id, Long parentId, String subSubCategory) {
        if (id == null || parentId == null || !validInput(subSubCategory)) {
            return false;
        }

        SubSubCategory ssc = em.find(SubSubCategory.class, id);
        SubCategory sc = em.find(SubCategory.class, parentId);

        if (sc == null || ssc == null) {
            return false;
        }

        ssc.setParentSubCategory(sc);
        ssc.setCategoryName(subSubCategory);

        em.merge(ssc);
        return true;
    }

    public boolean updateSubSubCategoryName(Long id, String subSubCategory) {
        if (id == null || !validInput(subSubCategory)) {
            return false;
        }

        SubSubCategory ssc = em.find(SubSubCategory.class, id);

        if (ssc == null) {
            return false;
        }

        ssc.setCategoryName(subSubCategory);

        em.merge(ssc);
        return true;
    }

    public List<Category> getCategories() {
        Query query = em.createNamedQuery(Category.GET_CATEGORIES);

        return query.getResultList();
    }

    public Category getCategory(String categoryName) {
        Query query = em.createNamedQuery(Category.GET_CATEGORY_BY_NAME);
        query.setParameter(1, formatInput(categoryName));

        try {
            return (Category) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Category getCategory(Long id) {
        Query query = em.createNamedQuery(Category.GET_CATEGORY_BY_ID);
        query.setParameter(1, id);

        try {
            return (Category) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isCategoryPresent(Long id) {
        return em.find(Category.class, id) != null;
    }

    public int deleteCategory(Long id) {
        Query query = em.createQuery("delete from Category c where c.id = ?1");
        query.setParameter(1, id);

        return query.executeUpdate();
    }

    public List<SubCategory> getSubCategoriesByParentId(Long categoryId) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORIES_BY_PARENT_ID);
        query.setParameter(1, categoryId);

        return query.getResultList();
    }

    public SubCategory getSubCategoryById(Long id) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORY_BY_ID);
        query.setParameter(1, id);

        try {
            return (SubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public SubCategory getSubCategoryByName(String categoryName) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORY_BY_NAME);
        query.setParameter(1, formatInput(categoryName));

        try {
            return (SubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SubCategory> getAllSubCategories() {
        Query query = em.createNamedQuery(SubCategory.GET_ALL_SUBCATEGORIES);

        return query.getResultList();
    }

    public boolean isSubCategoryPresent(Long id) {
        return em.find(SubCategory.class, id) != null;
    }

    public int deleteSubCategory(Long id) {
        Query query = em.createQuery("delete from SubCategory s where s.id = ?1");
        query.setParameter(1, id);

        return query.executeUpdate();
    }

    public List<SubSubCategory> getSubSubCategoriesByParentId(Long id) {
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORIES_BY_PARENT_ID);
        query.setParameter(1, id);

        return query.getResultList();
    }

    public SubSubCategory getSubSubCategoryById(Long id) {
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORY_BY_ID);
        query.setParameter(1, id);

        return (SubSubCategory) query.getSingleResult();
    }

    public SubSubCategory getSubSubCategoryByName(String categoryName) {
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORY_BY_NAME);
        query.setParameter(1, formatInput(categoryName));

        try {
            return (SubSubCategory) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SubSubCategory> getAllSubSubCategories() {
        Query query = em.createNamedQuery(SubSubCategory.GET_ALL_SUBSUBCATEGORIES);

        return query.getResultList();
    }

    public boolean isSubSubCategoryPresent(Long id) {
        return em.find(SubSubCategory.class, id) != null;
    }

    public List<Category> getAllCategoriesWithQuizzes() {
        Set<Category> categories = new HashSet<>();

        quizEJB.getAllQuizzes(MAX_FROM_DB).forEach(q ->
                categories.add(q.getParentSubSubCategory().getParentSubCategory().getParentCategory()));

        return new ArrayList<>(categories);
    }

    public List<SubSubCategory> getAllSubSubCategoryWithQuizzes() {
        Set<SubSubCategory> subSubCategories = new HashSet<>();

        quizEJB.getAllQuizzes(MAX_FROM_DB).forEach(q ->
                subSubCategories.add(q.getParentSubSubCategory()));

        return new ArrayList<>(subSubCategories);
    }

    public int deleteSubSubCategory(Long id) {
        Query query = em.createQuery("delete from SubSubCategory s where s.id = ?1");
        query.setParameter(1, id);

        return query.executeUpdate();
    }
}
