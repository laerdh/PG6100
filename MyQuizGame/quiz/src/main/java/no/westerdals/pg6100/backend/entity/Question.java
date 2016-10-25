package no.westerdals.pg6100.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Question {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String question;

    @NotNull
    private String correctAnswer;

    @NotNull
    private List<String> answers;

    @ManyToOne
    private SubSubCategory parentCategory;


    public Question() {}


    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }

    public String getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(String answer) { this.correctAnswer = correctAnswer; }

    public List<String> getAnswers() { return answers; }

    public void setAnswers(List<String> answers) { this.answers = answers; }

    public SubSubCategory getParentCategory() { return parentCategory; }

    public void setParentCategory(SubSubCategory parentCategory) { this.parentCategory = parentCategory; }
}
