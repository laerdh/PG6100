package no.westerdals.pg6100.rest.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.rest.dto.CategoryDto;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;

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
        String categoryName = "Sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(categoryName.toLowerCase()));
    }

    @Test
    public void testCreateAndDeleteCategory() {
        String categoryName = "Sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .delete("/id/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testUpdateCategory() {
        String categoryName = "Sports";
        CategoryDto dto = new CategoryDto(null, categoryName);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(categoryName.toLowerCase()));

        // Update categoryname
        categoryName = "Music";
        dto = new CategoryDto(null, categoryName);
        dto.id = Long.parseLong(id);

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
