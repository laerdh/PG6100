package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String question;

    @NotNull
    private String correctAnswer;

    @ElementCollection
    private List<String> answers;

    @ManyToOne
    private SubSubCategory parentSubSubCategory;


    public Question() {}


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }

    public String getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public List<String> getAnswers() {
        if (answers == null) {
            return new ArrayList<>();
        }
        return answers;
    }

    public void setAnswers(List<String> answers) { this.answers = answers; }

    public SubSubCategory getParentSubSubCategory() { return parentSubSubCategory; }

    public void setParentSubSubCategory(SubSubCategory parentSubSubCategory) { this.parentSubSubCategory = parentSubSubCategory; }
}
