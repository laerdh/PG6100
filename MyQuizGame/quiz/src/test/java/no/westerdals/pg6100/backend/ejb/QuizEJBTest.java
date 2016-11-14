package no.westerdals.pg6100.backend.ejb;

import no.westerdals.pg6100.backend.entity.Category;
import no.westerdals.pg6100.backend.entity.Quiz;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QuizEJBTest {

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private CategoryEJB categoryEJB;

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
    public void testAddQuizQuestion() throws Exception {
        Long categoryId = createCategories("Sports", "Football", "Premier League");

        List<String> answers = Arrays.asList("David Beckham", "Jimmy Floyd Hasselbaink", "Eric Cantona", "Alan Shearer");
        int correctAnswer = 3;

        Long id = quizEJB.createQuiz(categoryId, "Who is PL all-time topscorer?", answers, correctAnswer);
        assertNotNull(id);
    }

    @Test
    public void testAddQuizQuestionWithEmptyStringShouldFail() throws Exception {
        Long categoryId = createCategories("Sports", "Football", "Premier League");

        Long id = quizEJB.createQuiz(categoryId, "", new ArrayList<>(), null);
        assertNull(id);
    }

    @Test
    public void testAddQuizQuestionWithNullShouldFail() throws Exception {
        Long categoryId = createCategories("Sports", "Football", "Premier League");

        Long id = quizEJB.createQuiz(categoryId, null, null, null);
        assertNull(id);
    }

    @Test
    public void testGetQuizQuestion() throws Exception {
        Long categoryId = createCategories("Sports", "Football", "Premier League");

        List<String> answers = Arrays.asList("David Beckham", "Jimmy Floyd Hasselbaink", "Eric Cantona", "Alan Shearer");
        int correctAnswer = 3;

        Long id = quizEJB.createQuiz(categoryId, "Who is PL all-time topscorer?", answers, correctAnswer);
        assertNotNull(id);

        Quiz q = quizEJB.getQuiz(id);


        assertEquals(answers.get(correctAnswer), q.getAnswers().get(q.getCorrectAnswer()));
    }

    @Test
    public void testUpdateQuizQuestion() throws Exception {
        Long categoryId = categoryEJB.createCategory("Sports");
        Long subCategoryId = categoryEJB.createSubCategory(categoryId, "Football");
        Long subSubCategoryId = categoryEJB.createSubSubCategory(subCategoryId, "Premier League");

        List<String> answers = new ArrayList<>(Arrays.asList("Diego Costa", "Sergio Aguero", "Zlatan Ibrahimovic", "Olivier Giroud"));
        int correctAnswer = 0;

        String question = "Who is La Ligas topscorer?";
        Long id = quizEJB.createQuiz(subSubCategoryId, question, answers, correctAnswer);
        assertNotNull(id);

        Quiz q = quizEJB.getQuiz(id);
        assertEquals(question, q.getQuestion());

        question = "Who is Premier Leagues topscorer?";
        assertTrue(quizEJB.updateQuiz(id, subSubCategoryId, question, answers, 0));

        assertEquals(question, quizEJB.getQuiz(id).getQuestion());
    }

    @Test
    public void testDeleteQuizQuestion() throws Exception {
        Long id = createQuizQuestion();

        int expected = quizEJB.getAllQuizzes().size();
        assertEquals(1, quizEJB.deleteQuiz(id));
        int actual = quizEJB.getAllQuizzes().size();

        assertEquals(expected - 1, actual);
    }

    public Long createCategories(String category, String subCategory, String subSubCategory) throws Exception {
        Long id = categoryEJB.createCategory(category);
        Long subCategoryId = categoryEJB.createSubCategory(id, subCategory);
        return categoryEJB.createSubSubCategory(subCategoryId, subSubCategory);
    }

    public Long createQuizQuestion() throws Exception {
        Long categoryId = createCategories("Sports", "Football", "Premier League");

        List<String> answers = Arrays.asList("David Beckham", "Jimmy Floyd Hasselbaink", "Eric Cantona", "Alan Shearer");
        int correctAnswer = 3;

        Long id = quizEJB.createQuiz(categoryId, "Who is PLs all-time topscorer?", answers, correctAnswer);

        return id;
    }

}