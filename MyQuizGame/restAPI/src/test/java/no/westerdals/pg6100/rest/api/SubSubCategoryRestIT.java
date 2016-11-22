package no.westerdals.pg6100.rest.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import no.westerdals.pg6100.rest.dto.SubSubCategoryDto;
import org.junit.Before;
import org.junit.Test;

public class SubSubCategoryRestIT extends RestTestBase {

    @Before
    public void setBasePath() {
        RestAssured.basePath = BASE_PATH + SUBSUBCATEGORY_PATH;
    }

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football",
                categoryId));
        setBasePath();
        String categoryName = "premier league";

        SubSubCategoryDto dto = new SubSubCategoryDto(null, categoryName, subCategoryId);

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        SubSubCategoryDto response = given()
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.categoryName, categoryName);
        assertEquals(response.parentCategoryId, subCategoryId);
    }

    @Test
    public void testCreateAndDeleteSubSubCategory() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        setBasePath();
        String categoryName = "world cup";

        SubSubCategoryDto dto = new SubSubCategoryDto(null, categoryName, subCategoryId);

        get().then().statusCode(200).body("size()", is(0));

        String id = postJson(dto);

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .delete("/{id}")
                .then()
                .statusCode(204);

        get().then().statusCode(200).body("size()", is(0));
    }

    @Test
    public void testCreateAndUpdateSubSubCategory() {
        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        setBasePath();
        String categoryName = "serie a";

        SubSubCategoryDto dto = new SubSubCategoryDto(null, categoryName, subCategoryId);

        String id = postJson(dto);

        // Update subsubcategory name
        categoryName = "la liga";
        dto = new SubSubCategoryDto(Long.parseLong(id), categoryName, subCategoryId);

        given().contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(dto)
                .put("/{id}")
                .then()
                .statusCode(200);

        SubSubCategoryDto response = given()
                .pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract().as(SubSubCategoryDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.categoryName, categoryName);
        assertEquals(response.parentCategoryId, subCategoryId);
    }

    @Test
    public void testCreateAndUpdateSubSubCategoryName() {
        String categoryName = "premier league";

        Long categoryId = Long.parseLong(createCategory("sports"));
        Long subCategoryId = Long.parseLong(createSubCategory("football", categoryId));
        Long id = Long.parseLong(createSubSubCategory(categoryName, subCategoryId));

        given().pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(categoryName));

        String newCategoryName = "la liga";

        given().contentType(ContentType.TEXT)
                .pathParam("id", id)
                .body(newCategoryName)
                .patch("/{id}")
                .then()
                .statusCode(200);

        given().pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("categoryName", is(newCategoryName));
    }
}
