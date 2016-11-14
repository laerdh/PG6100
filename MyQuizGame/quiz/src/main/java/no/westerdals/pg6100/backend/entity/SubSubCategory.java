package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES,
        query = "select s from SubSubCategory s"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY_BY_ID,
        query = "select s from SubSubCategory s where s.id = ?1"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY_BY_NAME,
        query = "select s from SubSubCategory s where s.categoryName = ?1"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORIES_BY_PARENT_NAME,
        query = "select s from SubSubCategory s where s.parentSubCategory.categoryName = ?1"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORIES_BY_PARENT_ID,
        query = "select s from SubSubCategory s where s.parentSubCategory.id = ?1")
})
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET_ALL_SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY_BY_ID = "GET_SUBSUBCATEGORY_BY_ID";
    public static final String GET_SUBSUBCATEGORY_BY_NAME = "GET_SUBSUBCATEGORY_BY_NAME";
    public static final String GET_SUBSUBCATEGORIES_BY_PARENT_NAME = "GET_SUBSUBCATEGORIES_BY_PARENT_NAME";
    public static final String GET_SUBSUBCATEGORIES_BY_PARENT_ID = "GET_SUBSUBCATEGORIES_BY_PARENT_ID";

    @Id @GeneratedValue
    private Long id;

    @Size(min = 1, max = 256)
    private String categoryName;

    @ManyToOne
    private SubCategory parentSubCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSubSubCategory")
    private List<Quiz> quiz;


    public SubSubCategory() {}


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public SubCategory getParentSubCategory() { return parentSubCategory; }

    public void setParentSubCategory(SubCategory parentSubCategory) { this.parentSubCategory = parentSubCategory; }

    public List<Quiz> getQuiz() {
        if (quiz == null) {
            return new ArrayList<>();
        }
        return quiz;
    }

    public void setQuiz(List<Quiz> quiz) { this.quiz = quiz; }
}
