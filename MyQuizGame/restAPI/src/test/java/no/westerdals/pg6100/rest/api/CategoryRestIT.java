package no.westerdals.pg6100.rest.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import no.westerdals.pg6100.rest.dto.QuizDto;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class CategoryRestIT extends RestTestBase {

    @Before
    public void setBasePath() {
        RestAssured.basePath = BASE_PATH + CATEGORY_PATH;
    }

    @Test
    public void testCleanDB() {

        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAndGetCategory() {
        String categoryName = "sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        CategoryDto response = given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .extract().as(CategoryDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.categoryName, categoryName);
    }

    @Test
    public void testCreateAndDeleteCategory() {
        String categoryName = "sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .delete("/id/{id}")
                .then()
                .statusCode(204);

        get().then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testCreateAndUpdateCategory() {
        String categoryName = "sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        String id = postJson(dto);

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(categoryName));

        // Update categoryname
        categoryName = "music";
        dto = new CategoryDto(Long.parseLong(id), categoryName);

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(dto)
                .put("/id/{id}")
                .then()
                .statusCode(200);

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(categoryName));
    }

    @Test
    public void testCategoriesWithQuizzes() {
        Long cat1 = Long.parseLong(createCategory("sports"));
        Long cat2 = Long.parseLong(createCategory("music"));

        Long subCat1 = Long.parseLong(createSubCategory("football", cat1));
        Long subCat2 = Long.parseLong(createSubCategory("rock", cat2));
        Long subCat3 = Long.parseLong(createSubCategory("classical", cat2));

        Long subSubCat1 = Long.parseLong(createSubSubCategory("bands", subCat2));
        Long subSubCat2 = Long.parseLong(createSubSubCategory("premier league", subCat1));

        QuizDto dto = createQuiz(subSubCat2);
        RestAssured.basePath = BASE_PATH + QUIZ_PATH;
        String id = postJson(dto);

        RestAssured.basePath = BASE_PATH + CATEGORY_WITH_QUIZZES_PATH;
        get().then().statusCode(200).body("size()", is(1));
        get().then().statusCode(200).body("categoryName", hasItem("sports"));
    }
}
