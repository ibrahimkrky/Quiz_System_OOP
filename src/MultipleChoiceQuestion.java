import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    public MultipleChoiceQuestion(String text, int points) {
        super(text, points);
    }

    @Override
    public boolean checkAnswer(String answer) {
        return false;
    }
}
