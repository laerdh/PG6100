package no.westerdals.pg6100.rest.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import org.junit.Before;
import org.junit.Test;


public class SubCategoryRestIT extends RestTestBase {

    @Before
    public void setBasePath() {
        RestAssured.basePath = BASE_PATH + SUBCATEGORY_PATH;
    }

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    /*
        https://github.com/rest-assured/rest-assured/issues/741

        There is a bug in RestAssured when trying to compare
        Long values within a JSON body.
        A workaround is to extract the body as a Dto and then use
        regular Assert to check for equality.
     */
    @Test
    public void testCreateAndGetSubCategory() {
        String categoryId = createCategory("sports");
        setBasePath();

        String subCategoryName = "football";
        SubCategoryDto dto = new SubCategoryDto(null, subCategoryName,
                Long.parseLong(categoryId));

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        SubCategoryDto response = given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .extract().as(SubCategoryDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.categoryName, subCategoryName);
        assertEquals(response.parentCategoryId.toString(), categoryId);
    }

    @Test
    public void testCreateAndDeleteSubCategory() {
        String categoryId = createCategory("music");
        setBasePath();

        String subCategoryName = "rock";
        SubCategoryDto dto = new SubCategoryDto(null, subCategoryName,
                Long.parseLong(categoryId));

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
    public void testCreateAndUpdateSubCategory() {
        String categoryId = createCategory("music");
        setBasePath();

        String subCategoryName = "rock";
        SubCategoryDto dto = new SubCategoryDto(null, subCategoryName,
                Long.parseLong(categoryId));

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        // Update subcategoryname
        subCategoryName = "classical";
        dto = new SubCategoryDto(Long.parseLong(id), subCategoryName,
                Long.parseLong(categoryId));

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
                .body("categoryName", is(subCategoryName));
    }
}
