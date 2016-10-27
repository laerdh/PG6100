package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.SubCategory;
import no.westerdals.pg6100.backend.entity.SubSubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static no.westerdals.pg6100.backend.validation.InputValidation.formatInput;
import static no.westerdals.pg6100.backend.validation.InputValidation.validInput;

@Stateless
public class CategoryEJB {

    @PersistenceContext
    private EntityManager em;


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

    public Long createSubCategory(String parentCategory, String subCategory) {
        if (!validInput(parentCategory, subCategory)) {
            return null;
        }

        Category categoryExist = getCategory(formatInput(parentCategory));
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

    public Long createSubSubCategory(String parentCategory, String subSubCategory) {
        if (!validInput(parentCategory, subSubCategory)) {
            return null;
        }

        SubCategory subCategoryExist = getSubCategoryByName(formatInput(parentCategory));
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

    public int deleteCategory(String categoryName) {
        Query query = em.createQuery("delete from Category c where c.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
    }

    public List<SubCategory> getSubCategoriesByParentName(String category) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORIES_BY_PARENT_NAME);
        query.setParameter(1, formatInput(category));

        return query.getResultList();
    }

    public List<SubCategory> getSubCategoriesByParentId(Long categoryId) {
        Query query = em.createNamedQuery(SubCategory.GET_SUBCATEGORIES_BY_PARENT_ID);
        query.setParameter(1, categoryId);

        return query.getResultList();
    }

    public SubCategory getSubCategorybyId(Long id) {
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

    public int deleteSubCategory(String categoryName) {
        Query query = em.createQuery("delete from SubCategory s where s.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
    }

    public List<SubSubCategory> getSubSubCategoriesByParentName(String subCategory) {
        Query query = em.createNamedQuery(SubSubCategory.GET_SUBSUBCATEGORIES_BY_PARENT_NAME);
        query.setParameter(1, formatInput(subCategory));

        return query.getResultList();
    }

    public List<SubSubCategory> getSubSubCategoriesByParentID(Long id) {
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

    public int deleteSubSubCategory(String categoryName) {
        Query query = em.createQuery("delete from SubSubCategory s where s.categoryName = ?1");
        query.setParameter(1, formatInput(categoryName));

        return query.executeUpdate();
    }
}
