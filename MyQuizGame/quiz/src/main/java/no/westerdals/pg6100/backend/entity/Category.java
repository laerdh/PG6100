package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = Category.GET_CATEGORIES,
            query = "select c from Category c"),
    @NamedQuery(name = Category.GET_CATEGORY,
            query = "select c from Category c where c.categoryName = ?1")
})
public class Category {

    public static final String GET_CATEGORIES = "GET_CATEGORIES";
    public static final String GET_CATEGORY = "GET_CATEGORY";

    @Id
    @Size(min = 1, max = 256)
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentCategory")
    private List<SubCategory> subCategories;


    public Category() {}


    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public List<SubCategory> getSubCategories() {
        if (subCategories == null) {
            return new ArrayList<>();
        }
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) { this.subCategories = subCategories; }
}
