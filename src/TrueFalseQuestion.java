public class TrueFalseQuestion extends Question{
    private boolean correctAnswer;

    public TrueFalseQuestion(String text, int points, boolean correctAnswer) {
        super(text, points);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {
        return String.valueOf(correctAnswer).equalsIgnoreCase(answer);
    }

    public void displayOptions() {
        System.out.println("True / False");
    }
}

