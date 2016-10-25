package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES,
        query = "select s from SubSubCategory s"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORIES,
        query = "select s from SubSubCategory s where s.parentSubCategory.categoryName = ?1"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY,
        query = "select s from SubSubCategory s where s.categoryName = ?1")
})
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET_ALL_SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORIES = "GET_SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY = "GET_SUBSUBCATEGORY";

    @Id
    private String categoryName;

    @ManyToOne
    private SubCategory parentSubCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSubSubCategory")
    private List<Question> questions;


    public SubSubCategory() {}


    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public SubCategory getParentSubCategory() { return parentSubCategory; }

    public void setParentSubCategory(SubCategory parentSubCategory) { this.parentSubCategory = parentSubCategory; }

    public List<Question> getQuestions() {
        if (questions == null) {
            return new ArrayList<>();
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) { this.questions = questions; }
}
