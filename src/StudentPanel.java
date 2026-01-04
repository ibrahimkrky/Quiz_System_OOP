import java.util.ArrayList;
import java.util.Scanner;

public class StudentPanel {
    private Scanner scanner;

    public StudentPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        Quiz selectedQuiz = selectQuizFromList();
        if (selectedQuiz == null) return;

        FileHelper.loadQuestionsForQuiz(selectedQuiz);
        
        System.out.print("Ogrenci Adi: ");
        String name = scanner.nextLine();
        System.out.print("Ogrenci No: ");
        String id = scanner.nextLine();
        
        selectedQuiz.setStudent(new Student(name, id));
        selectedQuiz.start(scanner); 
    }

    private Quiz selectQuizFromList() {
        ArrayList<Quiz> quizzes = FileHelper.loadQuizList();
        if (quizzes.isEmpty()) {
            System.out.println("Sistemde kayitli hic quiz yok.");
            return null;
        }

        System.out.println("\n--- MEVCUT SINAVLAR ---");
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz q = quizzes.get(i);
            System.out.println((i+1) + ". " + q.getName() + " [" + q.getDifficulty() + "]");
        }
        System.out.print("Seciminiz (Iptal icin 0): ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == -1) return null;
            if (index >= 0 && index < quizzes.size()) {
                return quizzes.get(index);
            }
        } catch (NumberFormatException e) {}
        System.out.println("Gecersiz secim.");
        return null;
    }
}