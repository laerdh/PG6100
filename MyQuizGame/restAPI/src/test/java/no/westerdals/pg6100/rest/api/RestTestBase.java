package no.westerdals.pg6100.rest.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.rest.api.utils.JBossUtil;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import no.westerdals.pg6100.rest.dto.QuizDto;
import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class RestTestBase {

    protected static final String BASE_PATH = "/myquizgame/api";
    protected static final String QUIZ_PATH = "/quizzes";
    protected static final String CATEGORY_PATH = "/categories";
    protected static final String SUBCATEGORY_PATH = "/subcategories";
    protected static final String SUBSUBCATEGORY_PATH = "/subsubcategories";
    protected static final String CATEGORY_WITH_QUIZZES_PATH = "/categories/withQuizzes";
    protected static final String SUBSUBCATEGORY_WITH_QUIZZES_PATH = CATEGORY_WITH_QUIZZES_PATH + "/subsubcategories";

    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Before
    @After
    public void clean() {
        RestAssured.basePath = BASE_PATH + QUIZ_PATH;
        List<QuizDto> listQuiz = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(QuizDto[].class));

        listQuiz.forEach(dto -> deleteResource(dto.id));

        get().then().statusCode(200).body("size()", is(0));

        RestAssured.basePath = BASE_PATH + SUBSUBCATEGORY_PATH;

        List<SubSubCategoryDto> listSubSubCategory = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto[].class));

        listSubSubCategory.forEach(dto -> deleteResource(dto.id));

        RestAssured.basePath = BASE_PATH + SUBCATEGORY_PATH;

        List<SubCategoryDto> listSubCategory = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(SubCategoryDto[].class));

        listSubCategory.forEach(dto -> deleteResource(dto.id));

        RestAssured.basePath = BASE_PATH + CATEGORY_PATH;

        List<CategoryDto> listCategory = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(CategoryDto[].class));

        listCategory.forEach(dto -> deleteResource(dto.id));

        RestAssured.basePath = BASE_PATH;
    }

    protected String createCategory(String name) {
        RestAssured.basePath = BASE_PATH + CATEGORY_PATH;
        CategoryDto dto = new CategoryDto(null, name);

        return postJson(dto);
    }

    protected String createSubCategory(String name, Long parentId) {
        RestAssured.basePath = BASE_PATH + SUBCATEGORY_PATH;
        SubCategoryDto dto = new SubCategoryDto(null, name, parentId);

        return postJson(dto);
    }

    protected String createSubSubCategory(String name, Long parentId) {
        RestAssured.basePath = BASE_PATH + SUBSUBCATEGORY_PATH;
        SubSubCategoryDto dto = new SubSubCategoryDto(null, name, parentId);

        return postJson(dto);
    }

    protected <T> String postJson(T type) {
        return given().contentType(ContentType.JSON)
                .body(type)
                .post()
                .then()
                .statusCode(201)
                .extract().asString();
    }

    protected void deleteResource(Long id) {
        given().pathParam("id", id)
                .delete("/{id}")
                .then().statusCode(204);
    }

    protected QuizDto createQuiz(Long parentId) {
        QuizDto dto = new QuizDto();
        dto.question = "Who is Premier League's topscorer?";
        dto.answers = new ArrayList<>(Arrays.asList("Diego Costa", "Sergio Aguero", "Zlatan Ibrahimovic", "Olivier Giroud"));
        dto.correctAnswer = 0;
        dto.parentCategoryId = parentId;

        return dto;
    }
}
