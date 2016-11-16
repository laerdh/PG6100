package no.westerdals.pg6100.rest.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

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
}
