package no.westerdals.pg6100.gameapi.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Game {

    @NotNull
    @JsonProperty
    private Long id;

    @JsonProperty
    private List<Long> quizzes;

    @NotNull
    @JsonProperty
    private Integer answered;

    @NotNull
    @JsonProperty
    private Integer totalQuizzes;


    public Game() {
        // Jackson deserialization
    }

    public Game(Long id, List<Long> quizzes, Integer answered, Integer totalQuizzes) {
        this.id = id;
        this.quizzes = quizzes;
        this.answered = answered;
        this.totalQuizzes = totalQuizzes;
    }


    public void setId(Long id ) { this.id = id; }

    public Long getId() { return id; }

    public void setQuizzes(List<Long> quizzes) { this.quizzes = quizzes; }

    public List<Long> getQuizzes() { return quizzes; }

    public void setAnswered(Integer answered) { this.answered = answered; }

    public Integer getAnswered() { return answered; }

    public void setTotalQuizzes(Integer totalQuizzes) {
        this.totalQuizzes = totalQuizzes; }

    public Integer getTotalQuizzes() {
        return totalQuizzes;
    }
}
