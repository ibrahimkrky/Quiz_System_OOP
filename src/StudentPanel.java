import java.util.ArrayList;
import java.util.Scanner;

public class StudentPanel {
    private Scanner scanner;

    public StudentPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void Start() {
        ArrayList<Quiz> quizzes = FileHelper.loadQuizList();
        if (quizzes.isEmpty()) {
            System.out.println("Quiz yok.");
            return;
        }
        for (int i=0; i < quizzes.size(); i++) System.out.println((i+1) + ". " + quizzes.get(i).getName());

        System.out.print("secim:");
        int index = Integer.parseInt(scanner.nextLine()) -1;
        Quiz selectedQuiz = quizzes.get(index);

        FileHelper.loadQuestonsForQuiz(selectedQuiz);

        System.out.print("Adiniz: ");
        String name = scanner.nextLine();
        System.out.print("Numaraniz: ");
        String id = scanner.nextLine();

        selectedQuiz.setStudent(new Student(name, id));
        selectedQuiz.start(scanner);
    }
}
