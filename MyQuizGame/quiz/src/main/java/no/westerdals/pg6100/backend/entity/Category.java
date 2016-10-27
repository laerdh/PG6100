package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = Category.GET_CATEGORIES,
            query = "select c from Category c"),
    @NamedQuery(name = Category.GET_CATEGORY_BY_NAME,
            query = "select c from Category c where c.categoryName = ?1"),
    @NamedQuery(name = Category.GET_CATEGORY_BY_ID,
            query = "select c from Category c where c.id = ?1")
})
public class Category {

    public static final String GET_CATEGORIES = "GET_CATEGORIES";
    public static final String GET_CATEGORY_BY_NAME = "GET_CATEGORY_BY_CATEGORY_NAME";
    public static final String GET_CATEGORY_BY_ID = "GET_CATEGORY_BY_CATEGORY_ID";

    @Id @GeneratedValue
    private Long id;

    @Size(min = 1, max = 256)
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentCategory")
    private List<SubCategory> subCategories;


    public Category() {}


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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
