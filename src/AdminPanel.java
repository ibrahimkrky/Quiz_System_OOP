import java.util.ArrayList;
import java.util.Scanner;

public class AdminPanel {
    private Scanner scanner;

    public AdminPanel(Scanner scanner) { 
        this.scanner = scanner;
    }

    public void Start() {
        while (true) {
            System.out.println("\n--- YONETICI MENU ---");
            System.out.println("1. Yeni Quiz Olustur");
            System.out.println("2. Mevcut Quize Soru Ekle/Duzenle");
            System.out.println("0. Ana Menuye Don");
            System.out.println("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            if ( choice.equals("1")) {
                createNewQuiz();
            } else if (choice.equals("2")) {
                Quiz selectedQuiz = selectQuizFromList();
                if (selectedQuiz != null) {
                    manageQuestions(selectedQuiz);
                }
            }
        }
    }

    private void createNewQuiz() {
        System.out.println("Quiz Adi: ");
        String name = scanner.nextLine();
        System.out.println("Zorluk (1-Kolay, 2-Orta, 3-Zor): ");
        int diffChoice = Integer.parseInt(scanner.nextLine());
        Difficulty diff = (diffChoice == 3) ? Difficulty.Zor : (diffChoice == 2) ? Difficulty.Orta : Difficulty.Kolay;
        FileHelper.createQuiz(name, diff);
        System.out.println("Quiz olusturuldu!");
    }

    private Quiz selectQuizFromList() {
        ArrayList<Quiz> quizzes = FileHelper.loadQuizList();
        if (quizzes.isEmpty()) {
            System.out.println("Kayitli quiz yok.");
            return null;
        }
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println((i+1) + ". " + quizzes.get(i).getName());
        }
        System.out.println("Secim: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) -1;
            if (index >= 0 && index < quizzes.size()) return quizzes.get(index);
        } catch (Exception e) {}
        return null;
    }
    private void manageQuestions(Quiz quiz) {
        quiz.getQuestions().clear();
        FileHelper.loadQuestonsForQuiz(quiz);

        while(true) {
            System.out.println("\n--- " + quiz.getName() + " YONETIM ---");
            System.out.println("1. Soru Ekle");
            System.out.println("2. Soru Sil");
            System.out.println("3. Soru Duzenle");
            System.out.println("4. Listele");
            System.out.println("0. Geri");
            System.out.println("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;
            if (choice.equals("1")) {
                String data = getQuestionDataFromUser();
                if (data != null) FileHelper.addLineToQuizFile(quiz.getFilename(), data);
            } 
            else if (choice.equals("2")) deleteQuestion(quiz);
            else if (choice.equals("3")) editQuestion(quiz);
            else if (choice.equals("4")) {
                quiz.getQuestions().clear();
                FileHelper.loadQuestonsForQuiz(quiz);
                for (Question q : quiz.getQuestions()) System.out.println(q.getText());
            }
        }
    }

    private void deleteQuestion (Quiz quiz) {
        ArrayList<String> lines = FileHelper.loadRawLines(quiz.getFilename());
        for(int i=0; i<lines.size(); i++) System.out.println((i+1) + ". " + lines.get(i));
        System.out.println("Silinecek no: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < lines.size()) {
                lines.remove(idx);
                FileHelper.rewriteQuizFile(quiz.getFilename(), lines);
            }
        } catch (Exception e) {}
    }

    private void editQuestion(Quiz quiz) {
        ArrayList<String> lines = FileHelper.loadRawLines(quiz.getFilename());
        for(int i=0; i<lines.size(); i++) System.out.println((i+1) + ". " + lines.get(i));
        System.out.println("Duzenlenecek no: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < lines.size()) {
                System.out.println("Eski veri silinip yenisi yazilacak.");
                String newData = getQuestionDataFromUser();
                if(newData != null) {
                    lines.set (idx, newData);
                    FileHelper.rewriteQuizFile(quiz.getFilename(), lines);
                }
            }
        } catch (Exception e) {}
    }

    private String getQuestionDataFromUser() {
        System.out.println("1. Coktan Secmeli, 2. Dogru/Yanlis");
        String type = scanner.nextLine();
        System.out.print("Soru: ");
        String text = scanner.nextLine();
        System.out.print("Puan: ");
        int points = Integer.parseInt(scanner.nextLine());
        StringBuilder sb = new StringBuilder();
        if (type.equals("1")) {
            StringBuilder opts = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                System.out.print((i+1) + ".Sik: ");
                opts.append(scanner.nextLine()).append(i<3?",":"");
            }
            System.out.print("Dogru cevap (1-4): ");
            int c = Integer.parseInt(scanner.nextLine()) -1 ;
            sb.append("MCQ;;;").append(text).append(";;;").append(points).append(";;;").append(opts).append(";;;").append(c);
        } else {
            System.out.print("Cevap (true/false): ");
            Boolean ans = Boolean.parseBoolean(scanner.nextLine());
            sb.append("TF;;;").append(text).append(";;;").append(points).append(";;;").append(ans);
        }
        return sb.toString();
    }
}
