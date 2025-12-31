import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {
    private String name;
    private Difficulty difficulty;
    private String filename;
    private ArrayList<Question> questions;
    private Student student;

    public Quiz(String name, Difficulty difficulty, String filename) {
        this.name = name;
        this.difficulty = difficulty;
        this.filename = filename;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    public String getName() {return name;}
    public Difficulty geDifficulty() {return difficulty;}
    public String getFilename() {return filename;}
    public ArrayList<Question> getQuestions() {return questions;}
    public void setStudent(Student student) {
        this.student = student;
    }

    public void start(Scanner scanner) {
        if(student == null) {
            System.out.println("Ogrenci secilmedi!");
            return;
        }
        System.out.println("Sinav Basliyor: " + name);
        for (Question q : questions) {
            System.out.println("SORU: " + q.getText());
        }
    }

}
