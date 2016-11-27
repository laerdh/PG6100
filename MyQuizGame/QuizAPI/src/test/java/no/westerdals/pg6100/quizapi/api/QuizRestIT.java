package no.westerdals.pg6100.quizapi.api;

import static io.restassured.RestAssured.*;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.quizapi.dto.QuizDto;
import no.westerdals.pg6100.quizapi.dto.QuizPostDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class QuizRestIT extends RestTestBase {

    @Before
    public void setBasePath() {
        RestAssured.basePath = BASE_PATH + QUIZ_PATH;
    }

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAndGetQuiz() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long subSubCategoryId = Long.parseLong(createSubSubCategory("premier league",
                subCategoryId));
        setBasePath();

        QuizPostDto dto = createQuiz(subSubCategoryId);

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        QuizDto response = given()
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().as(QuizDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.question, dto.question);
        assertEquals(response.answers, dto.answers);
        assertEquals(response.parentCategoryId, dto.parentCategoryId);
    }

    @Test
    public void testCreateAndDeleteQuiz() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long subSubCategoryId = Long.parseLong(createSubSubCategory("premier league",
                subCategoryId));
        setBasePath();

        QuizPostDto dto = createQuiz(subSubCategoryId);

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .delete("/{id}")
                .then()
                .statusCode(204);

        get().then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testCreateAndUpdateQuiz() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long subSubCategoryId = Long.parseLong(createSubSubCategory("premier league",
                subCategoryId));
        setBasePath();

        QuizPostDto dto = createQuiz(subSubCategoryId);

        String id = postJson(dto);

        // Update quiz
        String question = "Who is Chelsea FC topscoring midfielder?";
        List<String> answers = Arrays.asList("Dennis Wise", "Frank Lampard", "Didier Deschamps", "Frank Lebouf");
        Integer correctAnswer = 1;

        dto = new QuizPostDto(Long.parseLong(id), question, correctAnswer,
                answers, dto.parentCategoryId);

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(dto)
                .put("/{id}")
                .then()
                .statusCode(200);

        QuizDto response = given()
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().as(QuizDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.question, question);
        assertTrue(dto.answers.containsAll(answers));
    }

    @Test
    public void testCreateAndUpdateQuizQuestion() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long subSubCategoryId = Long.parseLong(createSubSubCategory("premier league", subCategoryId));
        setBasePath();

        QuizPostDto dto = createQuiz(subSubCategoryId);
        String id = postJson(dto);

        // Update question
        String newQuestion = "Who is the current topscorer in Premier League?";

        given().contentType(ContentType.TEXT)
                .pathParam("id", id)
                .body(newQuestion)
                .patch("/{id}")
                .then()
                .statusCode(200);

        given().pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("question", is(newQuestion));
    }

    @Test
    public void testGetCorrectAnswer() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long subSubCategoryId = Long.parseLong(createSubSubCategory("premier league", subCategoryId));
        setBasePath();

        QuizPostDto dto = createQuiz(subSubCategoryId);
        String id = postJson(dto);

        Integer expected = dto.correctAnswer;

        Integer response = given()
                .pathParam("id", id)
                .get("/{id}/answer")
                .then()
                .statusCode(200)
                .extract().as(Integer.class);

        assertTrue(expected.equals(response));
    }
}
