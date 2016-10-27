package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.utils.DeleterEJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEJBTest {

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.westerdals.pg6100.backend")
                .addAsResource("META-INF/persistence.xml");
    }


    @Before
    @After
    public void emptyDatabase() throws Exception {
        categoryEJB.getCategories().forEach(c -> deleterEJB.deleteEntityById(Category.class, c.getId()));
    }

    @Test
    public void testAddCategory() throws Exception {
        int expected = categoryEJB.getCategories().size();
        assertNotNull(categoryEJB.createCategory("Sports"));

        int actual = categoryEJB.getCategories().size();
        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddCategoryWithNullShouldFail() throws Exception {
        assertNull(categoryEJB.createCategory(null));
    }

    @Test
    public void testAddCategoryWithEmptyStringShouldFail() throws Exception {
        assertNull(categoryEJB.createCategory(""));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        String category = "Sports";
        assertNotNull(categoryEJB.createCategory(category));

        int expected = categoryEJB.getCategories().size();
        assertEquals(1, categoryEJB.deleteCategory(category));
        int actual = categoryEJB.getCategories().size();

        assertEquals(expected - 1, actual);
    }

    @Test
    public void testAddSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        assertNotNull(categoryEJB.createCategory(category));
        int expected = categoryEJB.getSubCategoriesByParentName(category).size();

        assertNotNull(categoryEJB.createSubCategory(category, subCategory));
        int actual = categoryEJB.getSubCategoriesByParentName(category).size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubCategoryWithNullShouldFail() throws Exception {
        createCategories("Sports", "Football", "Premier League");
        assertNull(categoryEJB.createSubCategory("Sports", null));
    }

    @Test
    public void testAddSubCategoryWithEmptyStringShouldFail() throws Exception {
        createCategories("Sports", "Football", "Premier League");
        assertNull(categoryEJB.createSubCategory("Sports", ""));
    }

    @Test
    public void testAddSubCategoryToNullCategoryShouldFail() throws Exception {
        assertNull(categoryEJB.createSubCategory(null, "Basketball"));
    }

    @Test
    public void testAddSubCategoryToEmptyCategoryShouldFail() throws Exception {
        assertNull(categoryEJB.createSubCategory("", "Football"));
    }

    @Test
    public void testDeleteSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        assertNotNull(categoryEJB.createCategory(category));
        assertNotNull(categoryEJB.createSubCategory(category, subCategory));

        int expected = categoryEJB.getSubCategoriesByParentName(category).size();
        assertEquals(1, categoryEJB.deleteSubCategory(subCategory));
        int actual = categoryEJB.getSubSubCategoriesByParentName(category).size();

        assertEquals(expected - 1, actual);
    }

    @Test
    public void testGetAllSubCategories() throws Exception {
        int expected = categoryEJB.getAllSubCategories().size();

        createCategories("Sports", "Football", "Premier League");
        createCategories("Sports", "Basket", "NBA");
        createCategories("Sports", "Ski", "Cross-country Skiing");

        int actual = categoryEJB.getAllSubCategories().size();

        assertEquals(expected + 3, actual);
    }

    @Test
    public void testAddSubSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";
        String subSubCategory = "Premier League";

        assertNotNull(categoryEJB.createCategory(category));
        assertNotNull(categoryEJB.createSubCategory(category, subCategory));

        int expected = categoryEJB.getAllSubSubCategories().size();
        assertNotNull(categoryEJB.createSubSubCategory(subCategory, subSubCategory));
        int actual = categoryEJB.getAllSubSubCategories().size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubSubCategoryWithEmptyStringShouldFail() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        assertNotNull(categoryEJB.createCategory(category));
        assertNotNull(categoryEJB.createSubCategory(category, subCategory));

        assertNull(categoryEJB.createSubSubCategory(subCategory, ""));
    }

    @Test
    public void testAddSubSubCategoryWithNullShouldFail() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        assertNotNull(categoryEJB.createCategory(category));
        assertNotNull(categoryEJB.createSubCategory(category, subCategory));

        assertNull(categoryEJB.createSubSubCategory(subCategory, null));
    }

    @Test
    public void testAddSubSubCategoryToInvalidSubCategoryShouldFail() throws Exception {
        assertNull(categoryEJB.createSubSubCategory("", "Premier League"));
        assertNull(categoryEJB.createSubSubCategory(null, "Premier League"));
    }

    @Test
    public void testGetAllSubSubCategories() throws Exception {
        int expected = categoryEJB.getAllSubSubCategories().size();

        createCategories("Sports", "Football", "Premier League");
        createCategories("Sports", "Football", "Championship");
        createCategories("Sports", "Football", "Serie A");

        int actual = categoryEJB.getAllSubSubCategories().size();

        assertEquals(expected + 3, actual);
    }

    @Test
    public void testGetSubSubCategory() throws Exception {
        int expected = categoryEJB.getSubSubCategoriesByParentName("Football").size();

        createCategories("Sports", "Football", "Premier League");
        createCategories("Sports", "Football", "Champions League");

        int actual = categoryEJB.getSubSubCategoriesByParentName("Football").size();

        assertEquals(expected + 2, actual);
    }

    @Test
    public void testDeleteSubSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";
        String subSubCategory = "Premier League";

        createCategories(category, subCategory, subSubCategory);

        int expected = categoryEJB.getSubSubCategoriesByParentName(subCategory).size();
        assertEquals(1, categoryEJB.deleteSubSubCategory(subSubCategory));
        int actual = categoryEJB.getSubSubCategoriesByParentName(subCategory).size();

        assertEquals(expected - 1, actual);
    }

    public void createCategories(String category, String subCategory, String subSubCategory) throws Exception {
        categoryEJB.createCategory(category);
        categoryEJB.createSubCategory(category, subCategory);
        categoryEJB.createSubSubCategory(subCategory, subSubCategory);
    }
}