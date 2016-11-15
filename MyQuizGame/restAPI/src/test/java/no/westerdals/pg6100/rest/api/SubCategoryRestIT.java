package no.westerdals.pg6100.rest.api;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import no.westerdals.pg6100.rest.dto.SubCategoryDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


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
        String categoryId = createCategory("Sports");

        String subCategoryName = "Football";
        SubCategoryDto dto = new SubCategoryDto(null, subCategoryName, Long.parseLong(categoryId));

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        SubCategoryDto response = given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .extract().as(SubCategoryDto.class);

        assertEquals(response.id.toString(), id);
        assertEquals(response.categoryName, subCategoryName.toLowerCase());
        assertEquals(response.parentCategoryId.toString(), categoryId);
    }
}
