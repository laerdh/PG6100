package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class SubSubCategory {

    @Id
    private String subSubCategory;

    @ManyToOne
    private SubCategory parentCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentCategory")
    private List<Question> questions;


    public SubSubCategory() {}


    public String getSubSubCategory() { return subSubCategory; }

    public void setSubSubCategory(String subSubCategory) { this.subSubCategory = subSubCategory; }

    public SubCategory getParentCategory() { return parentCategory; }

    public void setParentCategory(SubCategory parentCategory) { this.parentCategory = parentCategory; }

    public List<Question> getQuestions() { return questions; }

    public void setQuestions(List<Question> questions) { this.questions = questions; }
}
