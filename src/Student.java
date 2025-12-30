public class Student {
    private String name;
    private String studentId;
    private int score;  
    
    public Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.score = 0;
    }

    public void addScore(int points) {
        if (points > 0) this.score += points;
    }

    public String getName() { return name;}
    public String getStudentId() { return studentId; }
    public int getScore() { return score;}
}

