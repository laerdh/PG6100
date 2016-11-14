package no.westerdals.pg6100.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(name = Quiz.GET_ALL_QUIZZES,
        query = "select q from Quiz q"),
    @NamedQuery(name = Quiz.GET_QUIZ_BY_ID,
        query = "select q from Quiz q where q.id = ?1"),
    @NamedQuery(name = Quiz.GET_CATEGORY_QUIZZES,
        query = "select q from Quiz q where q.parentSubSubCategory.categoryName = ?1")
})
public class Quiz {

    public static final String GET_ALL_QUIZZES = "GET_ALL_QUIZZES";
    public static final String GET_QUIZ_BY_ID = "GET_QUIZ_BY_ID";
    public static final String GET_CATEGORY_QUIZZES = "GET_CATEGORY_QUIZZES";

    @Id @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 2, max = 1024)
    private String question;

    @NotNull
    private Integer correctAnswer;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> answers;

    @ManyToOne
    private SubSubCategory parentSubSubCategory;


    public Quiz() {}


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }

    public void setQuestion(String question) { this.question = question; }

    public Integer getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }

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
