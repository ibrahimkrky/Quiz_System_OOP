public class Student {
    private String name;
    private String studentID;
    private int score;

    public Student(String name, String studentID) {
        this.name = name;
        this.studentID = studentID;
        this.score = 0;
    }

    public void addScore(int points) {
        if (points > 0) {
            this.score += points;
        }
    }

    public String getName() {
        return name;
    }

    public String getStudentID() {
        return studentID;
    }

    public int getScore() {
        return score;
    }
}