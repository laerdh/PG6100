package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.Question;
import no.westerdals.pg6100.backend.entity.SubCategory;
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
public class QuizEJBTest {

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
        quizEJB.getCategories().forEach(c -> deleterEJB.deleteEntityById(Category.class, c.getCategoryName()));
    }

    @Test
    public void testAddCategory() throws Exception {
        int expected = quizEJB.getCategories().size();
        assertTrue(quizEJB.createCategory("Sports"));

        int actual = quizEJB.getCategories().size();
        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";

        assertTrue(quizEJB.createCategory(category));
        int expected = quizEJB.getSubCategories(category).size();

        assertTrue(quizEJB.createSubCategory(category, subCategory));
        int actual = quizEJB.getSubCategories(category).size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddSubSubCategory() throws Exception {
        String category = "Sports";
        String subCategory = "Football";
        String subSubCategory = "Premier League";

        assertTrue(quizEJB.createCategory(category));
        assertTrue(quizEJB.createSubCategory(category, subCategory));

        int expected = quizEJB.getAllSubSubCategories().size();
        assertTrue(quizEJB.createSubSubCategory(subCategory, subSubCategory));
        int actual = quizEJB.getAllSubSubCategories().size();

        assertEquals(expected + 1, actual);
    }

    @Test
    public void testAddQuizQuestion() throws Exception {
        createCategories("Sports", "Football", "Premier League");

        List<String> answers = Arrays.asList("David Beckham", "Jimmy Floyd Hasselbaink", "Eric Cantona", "Alan Shearer");
        String correctAnswer = "Alan Shearer";

        assertTrue(quizEJB.createQuestion("Premier League", "Who is PL all-time topscorer?", answers, correctAnswer));
    }

    public void createCategories(String category, String subCategory, String subSubCategory) throws Exception {
        quizEJB.createCategory(category);
        quizEJB.createSubCategory(category, subCategory);
        quizEJB.createSubSubCategory(subCategory, subSubCategory);
    }

}