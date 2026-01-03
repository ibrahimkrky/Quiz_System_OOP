import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Quiz {
    private String name;
    private Difficulty difficulty;
    private String filename;
    private ArrayList<Question> questions;
    private Student student;
    private long timeLimitSeconds = 60;
    private boolean isFinished = false;

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
        if(student == null || questions.isEmpty()) return;

        Collections.shuffle(questions);
        isFinished =false;

        System.out.println("Sinav Basliyor: " + name);
        long startTime = System.currentTimeMillis();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!isFinished) System.out.println("\n!!! SURE DOLDU !!! Enter'a basiniz.");
            }
        };
        timer.schedule(task, timeLimitSeconds * 1000);
        try {
            for (Question q : questions) {
                long remaining = timeLimitSeconds - (System.currentTimeMillis() - startTime) / 1000;
                if (remaining <= 0) break;

                System.out.println("KALAN SURE: " + remaining);
                System.out.println("SORU: " + q.getText());

                if (q instanceof MultipleChoiceQuestion) ((MultipleChoiceQuestion) q).displayOptions();
                else if (q instanceof TrueFalseQuestion) ((TrueFalseQuestion) q).displayOptions();

                String answer = "";
                if (scanner.hasNextLine()) answer = scanner.nextLine();

                if (q.checkAnswer(answer)) {
                    System.out.println("DOGRU");
                    student.addScore(q.getPoints());
                } else System.out.println("YANLIS");
            }
            isFinished = true;
        } catch(Exception e) {
        } finally {
            timer.cancel();
            System.out.println("PUANINIZ: " + student.getScore());
        }
    }
}
