package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.SubSubCategory;
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

import java.util.Arrays;
import java.util.List;

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
    public void testUpdateCategory() throws Exception {
        String categoryName = "sports";
        Long id = categoryEJB.createCategory(categoryName);

        assertEquals(categoryName, categoryEJB.getCategory(id).getCategoryName());
        categoryName = "music";
        assertTrue(categoryEJB.updateCategory(id, categoryName));

        assertEquals(categoryName, categoryEJB.getCategory(id).getCategoryName());
    }

    @Test
    public void testUpdateSubCategory() throws Exception {
        String categoryName = "sports";
        String subCategoryName = "football";
        Long id = categoryEJB.createCategory(categoryName);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategoryName);

        assertEquals(subCategoryName, categoryEJB.getSubCategoryById(subCategoryId).getCategoryName());
        subCategoryName = "hockey";

        assertTrue(categoryEJB.updateSubCategory(subCategoryId, id, subCategoryName));

        assertEquals(subCategoryName, categoryEJB.getSubCategoryById(subCategoryId).getCategoryName());
    }

    @Test
    public void testUpdateSubSubCategory() throws Exception {
        String categoryName = "sports";
        String subCategoryName = "football";
        String subSubCategoryName = "premier league";

        Long id = categoryEJB.createCategory(categoryName);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategoryName);
        Long subSubCategoryId = categoryEJB.createSubSubCategory(subCategoryId, subSubCategoryName);

        assertEquals(subSubCategoryName, categoryEJB.getSubSubCategoryById(subSubCategoryId).getCategoryName());
        subSubCategoryName = "world cup";

        assertTrue(categoryEJB.updateSubSubCategory(subSubCategoryId, subCategoryId, subSubCategoryName));

        assertEquals(subSubCategoryName, categoryEJB.getSubSubCategoryById(subSubCategoryId).getCategoryName());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        String category = "Sports";
        Long id = categoryEJB.createCategory(category);

        int expected = categoryEJB.getCategories().size();
        assertEquals(1, categoryEJB.deleteCategory(id));
        int actual = categoryEJB.getCategories().size();

        assertEquals(expected - 1, actual);
    }

    @Test
    public void testAddSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        Long id = categoryEJB.createCategory(category);
        int expected = categoryEJB.getSubCategoriesByParentName(category).size();

        assertNotNull(categoryEJB.createSubCategory(id, subCategory));
        int actual = categoryEJB.getSubCategoriesByParentName(category).size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubCategoryWithNullShouldFail() throws Exception {
        Long id = categoryEJB.createCategory("Sports");

        assertNull(categoryEJB.createSubCategory(id, null));
    }

    @Test
    public void testAddSubCategoryWithEmptyStringShouldFail() throws Exception {
        Long id = categoryEJB.createCategory("Sports");

        assertNull(categoryEJB.createSubCategory(id, ""));
    }

    @Test
    public void testAddSubCategoryToNullCategoryShouldFail() throws Exception {
        assertNull(categoryEJB.createSubCategory(null, "Basketball"));
    }

    @Test
    public void testDeleteSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);

        int expected = categoryEJB.getSubCategoriesByParentName(category).size();
        assertEquals(1, categoryEJB.deleteSubCategory(subCategoryId));
        int actual = categoryEJB.getSubSubCategoriesByParentName(category).size();

        assertEquals(expected - 1, actual);
    }

    @Test
    public void testGetAllSubCategories() throws Exception {
        int expected = categoryEJB.getAllSubCategories().size();

        Long id = categoryEJB.createCategory("Sports");

        categoryEJB.createSubCategory(id, "Football");
        categoryEJB.createSubCategory(id, "Basket");
        categoryEJB.createSubCategory(id, "Ski");

        int actual = categoryEJB.getAllSubCategories().size();

        assertEquals(expected + 3, actual);
    }

    @Test
    public void testAddSubSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";
        String subSubCategory = "Premier League";

        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);

        int expected = categoryEJB.getAllSubSubCategories().size();
        categoryEJB.createSubSubCategory(subCategoryId, subSubCategory);
        int actual = categoryEJB.getAllSubSubCategories().size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubSubCategoryWithEmptyStringShouldFail() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);

        assertNull(categoryEJB.createSubSubCategory(subCategoryId, ""));
    }

    @Test
    public void testAddSubSubCategoryWithNullShouldFail() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);

        assertNull(categoryEJB.createSubSubCategory(subCategoryId, null));
    }

    @Test
    public void testAddSubSubCategoryToInvalidSubCategoryShouldFail() throws Exception {
        assertNull(categoryEJB.createSubSubCategory(null, "Premier League"));
        assertNull(categoryEJB.createSubSubCategory(1240L, "Premier League"));
    }

    @Test
    public void testGetAllSubSubCategories() throws Exception {
        int expected = categoryEJB.getAllSubSubCategories().size();

        Long id = categoryEJB.createCategory("Sports");
        Long subCategoryId = categoryEJB.createSubCategory(id, "Football");

        categoryEJB.createSubSubCategory(subCategoryId, "Premier League");
        categoryEJB.createSubSubCategory(subCategoryId, "Championship");
        categoryEJB.createSubSubCategory(subCategoryId, "Serie A");

        int actual = categoryEJB.getAllSubSubCategories().size();

        assertEquals(expected + 3, actual);
    }

    @Test
    public void testGetSubSubCategory() throws Exception {
        int expected = categoryEJB.getSubSubCategoriesByParentName("Football").size();

        Long id = categoryEJB.createCategory("Sports");
        Long subCategoryId = categoryEJB.createSubCategory(id, "Football");

        categoryEJB.createSubSubCategory(subCategoryId, "Premier League");
        categoryEJB.createSubSubCategory(subCategoryId, "Champions League");

        int actual = categoryEJB.getSubSubCategoriesByParentName("Football").size();

        assertEquals(expected + 2, actual);
    }

    @Test
    public void testGetAllCategoriesWithQuiz() throws Exception {
        String categoryName = "Sports";
        Long categoryId = categoryEJB.createCategory(categoryName);

        categoryEJB.createCategory("Music");
        categoryEJB.createCategory("Science");
        Long subCategoryId = categoryEJB.createSubCategory(categoryId, "Football");
        Long subSubCategoryId = categoryEJB.createSubSubCategory(subCategoryId, "Premier League");

        quizEJB.createQuiz(subSubCategoryId, "Who is PLs all-time topscorer?", Arrays.asList("Alan Shearer", "Andy Cole", "Eric Cantona", "Peter Osgood"), 0);

        List<Category> actual = categoryEJB.getAllCategoriesWithQuizzes();

        assertEquals(1, actual.size());
        assertEquals(categoryName.toLowerCase(), actual.get(0).getCategoryName());
    }

    @Test
    public void testGetAllSubSubCategoriesWithQuiz() throws Exception {
        String subSubCategoryName = "Premier League";

        Long categoryId = categoryEJB.createCategory("Sports");

        Long subCategoryId = categoryEJB.createSubCategory(categoryId, "Football");
        Long subSubCategoryId = categoryEJB.createSubSubCategory(subCategoryId, "Premier League");
        categoryEJB.createSubSubCategory(subCategoryId, "World cup");

        quizEJB.createQuiz(subSubCategoryId, "Who is PLs all-time topscorer?", Arrays.asList("Alan Shearer", "Andy Cole", "Eric Cantona", "Peter Osgood"), 0);

        List<SubSubCategory> actual = categoryEJB.getAllSubSubCategoryWithQuizzes();

        assertEquals(1, actual.size());
        assertEquals(subSubCategoryName.toLowerCase(), actual.get(0).getCategoryName());
    }

    @Test
    public void testDeleteSubSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";
        String subSubCategory = "Premier League";

        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);
        Long subSubCategoryId = categoryEJB.createSubSubCategory(subCategoryId, subSubCategory);

        int expected = categoryEJB.getSubSubCategoriesByParentName(subCategory).size();
        assertEquals(1, categoryEJB.deleteSubSubCategory(subSubCategoryId));
        int actual = categoryEJB.getSubSubCategoriesByParentName(subCategory).size();

        assertEquals(expected - 1, actual);
    }
}