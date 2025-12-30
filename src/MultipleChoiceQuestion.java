import java.util.ArrayList;

public class MultipleChoiceQuestion extends Question {
    private ArrayList<String> options;
    private int correctOptionIndex;

    public MultipleChoiceQuestion(String text, int points, ArrayList<String> options, int correctOptionIndex) {
        super(text, points);
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    @Override
    public boolean checkAnswer(String answer) {
        try {
            int answerIndex = Integer.parseInt(answer) - 1;
            return answerIndex == correctOptionIndex;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void displayOptions() {
        for (int i=0; i < options.size(); i++) {
            System.out.println((i + 1) + ") " + options.get(i));
        }
    }

    public ArrayList<String> getOptions() { return options;}
}
