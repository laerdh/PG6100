package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.SubCategory;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

import java.util.List;

import static no.westerdals.pg6100.backend.validation.InputValidation.formatInput;
import static no.westerdals.pg6100.backend.validation.InputValidation.validInput;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    private EntityManager em;


    public CategoryEJB() {}


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
        c.setCategoryName(formatInput(subCategory));
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
        c.setCategoryName(formatInput(subSubCategory));
        c.setParentSubCategory(subCategoryExist);


        em.persist(c);
        subCategoryExist.getSubSubCategories().add(c);
        return true;
    }

    public List<Category> getCategories() {
        Query query = em.createNamedQuery(Category.GET_CATEGORIES);

        return query.getResultList();
    }

    public int deleteCategory(String categoryName) {
        Query query = em.createQuery("delete from Category c where c.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
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

    public int deleteSubCategory(String categoryName) {
        Query query = em.createQuery("delete from SubCategory s where s.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
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

    public int deleteSubSubCategory(String categoryName) {
        Query query = em.createQuery("delete from SubSubCategory s where s.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
    }
}
