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
    private long timeLimitSeconds;
    private boolean isFinished = false; 

    public Quiz(String name, Difficulty difficulty, String filename) {
        this.name = name;
        this.difficulty = difficulty;
        this.filename = filename;
        this.questions = new ArrayList<>();
        this.timeLimitSeconds = 20; 
    }

    public String getName() { return name; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getFilename() { return filename; }
    public ArrayList<Question> getQuestions() { return questions; }

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    public void start(Scanner scanner) {
        if (questions.isEmpty()) {
            System.out.println("Bu quizde henuz soru yok!");
            return;
        }
        
        if (student == null) {
            System.out.println("Once ogrenci girisi yapilmalidir!");
            return;
        }

        Collections.shuffle(questions);
        isFinished = false;
        
        System.out.println("\n=== " + name.toUpperCase() + " SINAVI (" + difficulty + ") ===");
        System.out.println("Sayin " + student.getName() + ", sinav basliyor!");
        System.out.println("TOPLAM SURENIZ: " + timeLimitSeconds + " Saniye.");
        System.out.println("------------------------------------------------");

        long startTime = System.currentTimeMillis();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!isFinished) { 
                    System.out.println("\n\n!!! SURE DOLDU !!!");
                    System.out.println("Lutfen bir tusa ve ENTER'a basarak sonucu gorunuz...");
                }
            }
        };
        timer.schedule(task, timeLimitSeconds * 1000); 

        try {
            for (Question q : questions) {
                long currentTime = System.currentTimeMillis();
                long remainingTime = timeLimitSeconds - (currentTime - startTime) / 1000;
                
                if (remainingTime <= 0) break;

                System.out.println("\n>> KALAN SURE: " + remainingTime + " saniye <<");
                System.out.println("SORU: " + q.getText() + " (" + q.getPoints() + " Puan)");
                
                if (q instanceof MultipleChoiceQuestion) {
                    ((MultipleChoiceQuestion) q).displayOptions();
                    System.out.print("Cevabiniz (1, 2, 3...): ");
                } else if (q instanceof TrueFalseQuestion) {
                    ((TrueFalseQuestion) q).displayOptions();
                    System.out.print("Cevabiniz (true/false): ");
                }

                String answer = "";
                if (scanner.hasNextLine()) {
                    answer = scanner.nextLine();
                }

                long checkTime = System.currentTimeMillis();
                if ((checkTime - startTime) / 1000 > timeLimitSeconds) {
                    System.out.println("!!! GEC KALDINIZ (Sure Doldu) !!!");
                    break; 
                }

                if (q.checkAnswer(answer)) {
                    System.out.println("-> DOGRU!");
                    student.addScore(q.getPoints());
                } else {
                    System.out.println("-> YANLIS!");
                }
                System.out.println("------------------------------------------------");
            }
            isFinished = true; 
            
        } catch (Exception e) {
            System.out.println("Bir hata olustu: " + e.getMessage());
        } finally {
            timer.cancel();
            long duration = (System.currentTimeMillis() - startTime) / 1000;
            if (duration > timeLimitSeconds) duration = timeLimitSeconds;
            showResult(duration);
        }
    }

    public void showResult(long duration) {
        System.out.println("\n********************************");
        System.out.println("          SINAV SONUCU          ");
        System.out.println("********************************");
        System.out.println("Quiz: " + name + " (" + difficulty + ")");
        System.out.println("Ogrenci: " + student.getName());
        System.out.println("Puan: " + student.getScore());
    }
}