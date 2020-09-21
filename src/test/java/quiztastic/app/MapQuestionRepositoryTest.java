package quiztastic.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quiztastic.core.Category;
import quiztastic.core.Question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapQuestionRepositoryTest {
    private MapQuestionRepository smallQuestionRepo;

    public static MapQuestionRepository getQuestionsSmallRepo() throws IOException, ParseException {
        InputStream s = MapQuestionRepository.class
                .getClassLoader()
                .getResourceAsStream("questions-small.tsv");
        if (s == null) throw new FileNotFoundException("No File");
        return MapQuestionRepository.fromQuestionReader(
                new QuestionReader(new InputStreamReader(s)));
    }

    @BeforeEach
    void createQuestionRepo () throws IOException, ParseException {
         smallQuestionRepo = getQuestionsSmallRepo();
    }

    @Test
    void shouldReadTheSmallQuestionFile() {
        // Test that we can generate the smallQuestionRepo
    }

    @Test
    void shouldContainAllCategories() {
        List<Category> categories = smallQuestionRepo.getCategories();
        assertTrue(categories.contains(new Category("LAKES & RIVERS")));
        assertTrue(categories.contains(new Category("INVENTIONS")));
        assertTrue(categories.contains(new Category("ANIMALS")));
    }

    @Test
    void lakesAndRiversShouldContainFourQuestions() throws IOException {
        List<Question> questions =
                smallQuestionRepo.getQuestionsWithCategory(new Category("LAKES & RIVERS"));
        assertEquals(4, questions.size());
    }

}