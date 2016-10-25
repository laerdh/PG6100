package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubCategory {

    @Id
    private String subCategory;

    @ManyToOne
    private Category parentCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentCategory")
    private List<SubSubCategory> subSubCategories;


    public SubCategory() {}


    public String getSubCategory() { return subCategory; }

    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }

    public Category getParentCategory() { return parentCategory; }

    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public List<SubSubCategory> getSubSubCategories() { return subSubCategories; }

    public void setSubSubCategories(List<SubSubCategory> subSubCategories) { this.subSubCategories = subSubCategories; }
}
