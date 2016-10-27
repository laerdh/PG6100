package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = SubCategory.GET_ALL_SUBCATEGORIES,
        query = "select s from SubCategory s"),
    @NamedQuery(name = SubCategory.GET_SUBCATEGORY_BY_ID,
        query = "select s from SubCategory s where s.id = ?1"),
    @NamedQuery(name = SubCategory.GET_SUBCATEGORY_BY_NAME,
        query = "select s from SubCategory s where s.categoryName = ?1"),
    @NamedQuery(name = SubCategory.GET_SUBCATEGORIES_BY_PARENT_NAME,
        query = "select s from SubCategory s where s.parentCategory.categoryName = ?1"),
    @NamedQuery(name = SubCategory.GET_SUBCATEGORIES_BY_PARENT_ID,
        query = "select s from SubCategory s where s.parentCategory.id = ?1")
})
public class SubCategory {

    public static final String GET_ALL_SUBCATEGORIES = "GET_ALL_SUBCATEGORIES";
    public static final String GET_SUBCATEGORY_BY_ID = "GET_SUBCATEGORY";
    public static final String GET_SUBCATEGORY_BY_NAME = "GET_SUBCATEGORY_BY_NAME";
    public static final String GET_SUBCATEGORIES_BY_PARENT_NAME = "GET_SUBCATEGORIES_BY_PARENT_NAME";
    public static final String GET_SUBCATEGORIES_BY_PARENT_ID = "GET_SUBCATEGORIES_BY_PARENT_ID";

    @Id @GeneratedValue
    private Long id;

    @Size(min = 1, max = 256)
    private String categoryName;

    @ManyToOne
    private Category parentCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSubCategory")
    private List<SubSubCategory> subSubCategories;


    public SubCategory() {}


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Category getParentCategory() { return parentCategory; }

    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public List<SubSubCategory> getSubSubCategories() {
        if (subSubCategories == null) {
            return new ArrayList<>();
        }
        return subSubCategories;
    }

    public void setSubSubCategories(List<SubSubCategory> subSubCategories) { this.subSubCategories = subSubCategories; }
}
