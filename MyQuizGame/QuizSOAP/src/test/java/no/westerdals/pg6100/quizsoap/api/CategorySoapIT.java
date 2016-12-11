package no.westerdals.pg6100.quizsoap.api;

import no.westerdals.pg6100.quizsoap.api.client.CategoryDto;
import no.westerdals.pg6100.quizsoap.api.client.CategorySoap;
import no.westerdals.pg6100.quizsoap.api.client.CategorySoapImplService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.ws.BindingProvider;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CategorySoapIT {

    private static CategorySoap ws;

    @BeforeClass
    public static void initClass() {
        CategorySoapImplService service = new CategorySoapImplService();
        ws = service.getCategorySoapImplPort();

        String url = "http://localhost:8080/quizsoap/CategorySoapImpl";

        ((BindingProvider)ws).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

    }

    @Before
    @After
    public void cleanData() {
        List<CategoryDto> list = ws.getCategories();

        list.stream().forEach(dto -> ws.deleteCategory(dto.getId()));
    }

    @Test
    public void testCleanDB() {
        List<CategoryDto> list = ws.getCategories();

        assertEquals(0, list.size());
    }

    @Test
    public void testCreateAndGet() {
        assertEquals(0, ws.getCategories().size());

        String categoryName = "sports";
        CategoryDto dto = new CategoryDto();
        dto.setCategoryName(categoryName);

        Long id = ws.createCategory(dto);
        assertNotNull(id);

        assertEquals(1, ws.getCategories().size());

        CategoryDto res = ws.getCategory(id);

        assertEquals(id, res.getId());
        assertEquals(categoryName, res.getCategoryName());
    }

    @Test
    public void testCreateCategoryWithIdShouldFail() {
        assertEquals(0, ws.getCategories().size());

        String categoryName = "sports";
        CategoryDto dto = new CategoryDto();
        dto.setId(1337L);
        dto.setCategoryName(categoryName);

        Long id = ws.createCategory(dto);
        assertNull(id);
    }

    @Test
    public void testCreateCategoryWithEmptyNameShouldFail() {
        assertEquals(0, ws.getCategories().size());

        CategoryDto dto = new CategoryDto();

        dto.setCategoryName("");
        assertNull(ws.createCategory(dto));

        dto.setCategoryName(null);
        assertNull(ws.createCategory(dto));
    }
}