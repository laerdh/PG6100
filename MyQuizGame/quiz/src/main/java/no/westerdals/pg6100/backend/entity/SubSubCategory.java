package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = SubSubCategory.GET_ALL_SUBSUBCATEGORIES,
        query = "select s from SubSubCategory s"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORIES,
        query = "select s from SubSubCategory s where s.parentSubCategory.subCategoryName = ?1"),
    @NamedQuery(name = SubSubCategory.GET_SUBSUBCATEGORY,
        query = "select s from SubSubCategory s where s.subSubCategoryName = ?1")
})
public class SubSubCategory {

    public static final String GET_ALL_SUBSUBCATEGORIES = "GET_ALL_SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORIES = "GET_SUBSUBCATEGORIES";
    public static final String GET_SUBSUBCATEGORY = "GET_SUBSUBCATEGORY";

    @Id
    private String subSubCategoryName;

    @ManyToOne
    private SubCategory parentSubCategory;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;


    public SubSubCategory() {}


    public String getSubSubCategoryName() { return subSubCategoryName; }

    public void setSubSubCategoryName(String subSubCategoryName) { this.subSubCategoryName = subSubCategoryName; }

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
