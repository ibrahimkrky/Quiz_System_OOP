import java.util.ArrayList;
import java.util.Scanner;

public class AdminPanel {
    private Scanner scanner;

    public AdminPanel(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        while(true) {
            System.out.println("\n--- YONETICI MENU ---");
            System.out.println("1. Yeni Quiz Olustur");
            System.out.println("2. Mevcut Quize Soru Ekle/Duzenle");
            System.out.println("3. Quiz Sil");
            System.out.println("0. Ana Menuye Don");
            System.out.print("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            if (choice.equals("1")) {
                createNewQuiz();
            } else if (choice.equals("2")) {
                Quiz selectedQuiz = selectQuizFromList();
                if (selectedQuiz != null) {
                    manageQuestions(selectedQuiz);
                }
            } else if (choice.equals("3")) { // <--- YENİ BLOK
                deleteQuiz();
            }
        }
    }

    private void createNewQuiz() {
        System.out.print("Quiz Adi (Orn: Matematik Vize): ");
        String name = scanner.nextLine();
        
        System.out.println("Zorluk Seviyesi:");
        System.out.println("1. KOLAY\n2. ORTA\n3. ZOR");
        System.out.print("Secim: ");
        int diffChoice = Integer.parseInt(scanner.nextLine());
        Difficulty diff = (diffChoice == 3) ? Difficulty.Zor : (diffChoice == 2) ? Difficulty.Orta : Difficulty.Kolay;

        FileHelper.createQuiz(name, diff);
        System.out.println("-> Quiz basariyla olusturuldu!");
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

    private void deleteQuiz() {
        ArrayList<Quiz> quizzes = FileHelper.loadQuizList();
        
        if (quizzes.isEmpty()) {
            System.out.println("Silinecek quiz bulunamadi.");
            return;
        }

        System.out.println("\n--- SILINECEK QUIZ'I SECINIZ ---");
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz q = quizzes.get(i);
            System.out.println((i+1) + ". " + q.getName() + " [" + q.getDifficulty() + "]");
        }
        
        System.out.print("Silmek istediginiz numara (Iptal icin 0): ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (index == -1) return;

            if (index >= 0 && index < quizzes.size()) {
                Quiz quizToDelete = quizzes.get(index);
                
                System.out.print("'" + quizToDelete.getName() + "' quizi ve tum sorulari silinecek. Emin misiniz? (E/H): ");
                String confirm = scanner.nextLine();
                
                if (confirm.equalsIgnoreCase("E")) {
                    FileHelper.deleteFile(quizToDelete.getFilename());
                    
                    quizzes.remove(index);
                    
                    FileHelper.updateQuizListFile(quizzes);
                    
                    System.out.println("-> Quiz basariyla silindi.");
                } else {
                    System.out.println("Silme islemi iptal edildi.");
                }
            } else {
                System.out.println("Gecersiz secim.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Hatali giris.");
        }
    }

    private void manageQuestions(Quiz quiz) {
        quiz.getQuestions().clear(); 
        FileHelper.loadQuestionsForQuiz(quiz);

        while(true) {
            System.out.println("\n--- " + quiz.getName().toUpperCase() + " SORU YONETIMI ---");
            System.out.println("1. Soru Ekle");
            System.out.println("2. Soru Sil");
            System.out.println("3. Soru Duzenle");
            System.out.println("4. Sorulari Listele");
            System.out.println("0. Geri Don");
            System.out.print("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            if (choice.equals("1")) {
                String data = getQuestionDataFromUser(quiz.getDifficulty());
                if (data != null) {
                    FileHelper.addLineToQuizFile(quiz.getFilename(), data);
                    System.out.println("-> Soru eklendi.");
                    quiz.getQuestions().clear();
                    FileHelper.loadQuestionsForQuiz(quiz);
                }
            } else if (choice.equals("2")) {
                deleteQuestion(quiz);
            } else if (choice.equals("3")) {
                editQuestion(quiz);
            } else if (choice.equals("4")) {
                System.out.println("\n--- SORU LISTESI ---");
                for(int i=0; i<quiz.getQuestions().size(); i++) {
                    System.out.println((i+1) + ". " + quiz.getQuestions().get(i).getText());
                }
            }
        }
    }

    private void deleteQuestion(Quiz quiz) {
        ArrayList<String> lines = FileHelper.loadRawLines(quiz.getFilename());
        if(lines.isEmpty()) { System.out.println("Silinecek soru yok."); return; }

        for(int i=0; i<lines.size(); i++) System.out.println((i+1) + ". " + lines.get(i).split(";;;")[1]);
        
        System.out.print("Silinecek no: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if(idx >= 0 && idx < lines.size()) {
                lines.remove(idx);
                FileHelper.rewriteQuizFile(quiz.getFilename(), lines);
                System.out.println("Silindi.");
                quiz.getQuestions().clear();
                FileHelper.loadQuestionsForQuiz(quiz);
            }
        } catch(Exception e) {}
    }

    private void editQuestion(Quiz quiz) {
        ArrayList<String> lines = FileHelper.loadRawLines(quiz.getFilename());
        if (lines.isEmpty()) {
            System.out.println("Duzenlenecek soru bulunamadi.");
            return;
        }

        System.out.println("\n--- DUZENLENECEK SORUYU SECINIZ ---");
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(";;;");
            System.out.println((i + 1) + ". " + parts[1] + " (" + parts[2] + " Puan)");
        }
        
        System.out.print("Seciminiz (Iptal icin 0): ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index == -1) return;

            if (index >= 0 && index < lines.size()) {
                String oldLine = lines.get(index);
                String newLine = null;

                if (oldLine.startsWith("MCQ")) {
                    newLine = editMCQPart(oldLine);
                } else if (oldLine.startsWith("TF")) {
                    newLine = editTFPart(oldLine);
                }

                if (newLine != null) {
                    lines.set(index, newLine);
                    FileHelper.rewriteQuizFile(quiz.getFilename(), lines);
                    System.out.println("-> Soru guncellendi.");
                    quiz.getQuestions().clear();
                    FileHelper.loadQuestionsForQuiz(quiz);
                } else {
                    System.out.println("Degisiklik yapilmadi.");
                }
            } else {
                System.out.println("Gecersiz numara.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Hatali giris.");
        }
    }

    private String getQuestionDataFromUser(Difficulty quizDiff) {
        System.out.println("1. Coktan Secmeli\n2. Dogru/Yanlis");
        String type = scanner.nextLine();
        System.out.print("Soru metni: ");
        String text = scanner.nextLine();
        System.out.print("Puan: ");
        int points = Integer.parseInt(scanner.nextLine());

        StringBuilder sb = new StringBuilder();
        if (type.equals("1")) {
            StringBuilder opts = new StringBuilder();
            System.out.println("4 Sikki giriniz:");
            for(int i=0; i<4; i++) {
                opts.append(scanner.nextLine());
                if(i<3) opts.append(",");
            }
            System.out.print("Dogru sik no (1-4): ");
            int correctIdx = Integer.parseInt(scanner.nextLine()) - 1;
            sb.append("MCQ;;;").append(text).append(";;;").append(points).append(";;;")
              .append(opts).append(";;;").append(correctIdx);
        } else if (type.equals("2")) {
            System.out.print("Dogru cevap (true/false): ");
            boolean ans = Boolean.parseBoolean(scanner.nextLine());
            sb.append("TF;;;").append(text).append(";;;").append(points).append(";;;").append(ans);
        } else return null;
        
        return sb.toString();
    }

    private String editMCQPart(String oldLine) {
        String[] parts = oldLine.split(";;;");
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String optionsStr = parts[3];
        int correctIdx = Integer.parseInt(parts[4]);

        while (true) {
            System.out.println("\n--- DUZENLEME MENUSU (MCQ) ---");
            System.out.println("1. Metin (" + text + ")");
            System.out.println("2. Puan (" + points + ")");
            System.out.println("3. Siklar");
            System.out.println("4. Dogru Cevap");
            System.out.println("5. KAYDET VE CIK");
            System.out.println("0. Iptal");
            System.out.print("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) return null;
            if (choice.equals("5")) break;

            switch (choice) {
                case "1": System.out.print("Yeni Metin: "); text = scanner.nextLine(); break;
                case "2": System.out.print("Yeni Puan: "); points = Integer.parseInt(scanner.nextLine()); break;
                case "3": 
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i<4; i++) {
                        System.out.print((i+1) + ". Sik: ");
                        sb.append(scanner.nextLine());
                        if(i<3) sb.append(",");
                    }
                    optionsStr = sb.toString();
                    break;
                case "4": System.out.print("Yeni Dogru Sik (1-4): "); correctIdx = Integer.parseInt(scanner.nextLine()) - 1; break;
            }
        }
        return "MCQ;;;" + text + ";;;" + points + ";;;" + optionsStr + ";;;" + correctIdx;
    }

    private String editTFPart(String oldLine) {
        String[] parts = oldLine.split(";;;");
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        boolean answer = Boolean.parseBoolean(parts[3]);

        while (true) {
            System.out.println("\n--- DUZENLEME MENUSU (TF) ---");
            System.out.println("1. Metin (" + text + ")");
            System.out.println("2. Puan (" + points + ")");
            System.out.println("3. Cevap (" + answer + ")");
            System.out.println("4. KAYDET VE CIK");
            System.out.println("0. Iptal");
            System.out.print("Secim: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) return null;
            if (choice.equals("4")) break;

            switch (choice) {
                case "1": System.out.print("Yeni Metin: "); text = scanner.nextLine(); break;
                case "2": System.out.print("Yeni Puan: "); points = Integer.parseInt(scanner.nextLine()); break;
                case "3": System.out.print("Yeni Cevap (true/false): "); answer = Boolean.parseBoolean(scanner.nextLine()); break;
            }
        }
        return "TF;;;" + text + ";;;" + points + ";;;" + answer;
    }
}
