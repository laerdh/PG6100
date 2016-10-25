package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = SubCategory.GET_ALL_SUBCATEGORIES,
        query = "select s from SubCategory s"),
    @NamedQuery(name = SubCategory.GET_SUBCATEGORIES,
        query = "select s from SubCategory s where s.parentCategory.categoryName = ?1")
})
public class SubCategory {

    public static final String GET_ALL_SUBCATEGORIES = "GET_ALL_SUBCATEGORIES";
    public static final String GET_SUBCATEGORIES = "GET_SUBCATEGORIES";

    @Id
    private String categoryName;

    @ManyToOne
    private Category parentCategory;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubSubCategory> subSubCategories;


    public SubCategory() {}


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
