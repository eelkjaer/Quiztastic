package quiztastic.app;

import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.domain.QuestionRepository;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapQuestionRepository implements QuestionRepository {
    private final HashMap<Category, List<Question>> questionsByCategory;

    public MapQuestionRepository(HashMap<Category, List<Question>> questionsByCategory) {
        this.questionsByCategory = questionsByCategory;
    }

    public static MapQuestionRepository fromQuestionReader(QuestionReader reader) throws IOException, ParseException {
        Question q;
        HashMap<Category, List<Question>> questionsByCategory = new HashMap<>();
        while((q=reader.readQuestion()) != null){
            List<Question> current = questionsByCategory.get(q.getCategory());

            if (current == null) {
                current = new ArrayList<>();

                questionsByCategory.put(q.getCategory(), current);
            }
            current.add(q);
        }

        return new MapQuestionRepository(questionsByCategory);
    }

    @Override
    public List<Category> getCategories() {
        return List.copyOf(questionsByCategory.keySet());
    }

    @Override
    public List<Question> getQuestionsWithCategory(Category category) {
        return List.copyOf(questionsByCategory.get(category));
    }

    @Override
    public Iterable<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        for (List<Question> l : questionsByCategory.values()) {
            questions.addAll(l);
        }
        return questions;
    }
}
