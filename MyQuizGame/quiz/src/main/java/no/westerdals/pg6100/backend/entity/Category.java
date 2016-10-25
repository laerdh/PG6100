package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    private String category;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentCategory")
    private List<SubCategory> subCategories;


    public Category() {}


    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public List<SubCategory> getSubCategories() { return subCategories; }

    public void setSubCategories(List<SubCategory> subCategories) { this.subCategories = subCategories; }
}
