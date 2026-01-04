public abstract class Question implements Gradable {
    private String text;
    private int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public abstract boolean checkAnswer(String answer);

    public String getText() {
        return text;
    }

    @Override
    public int getPoints() {
        return points;
    }
}
