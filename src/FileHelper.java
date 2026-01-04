import java.io.*;
import java.util.ArrayList;

public class FileHelper {
    private static final String QUIZ_LIST_FILE = "quizzes.txt";

    public static ArrayList<Quiz> loadQuizList() {
        ArrayList<Quiz> quizzes = new ArrayList<>();
        File file = new File(QUIZ_LIST_FILE);
        if (!file.exists()) return quizzes;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";;;");
                if (parts.length >= 3) {
                    quizzes.add(new Quiz(parts[0], Difficulty.valueOf(parts[1]), parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Quiz listesi okunamadi: " + e.getMessage());
        }
        return quizzes;
    }

    public static void createQuiz(String name, Difficulty difficulty) {
        String safeName = name.replaceAll("\\s+", "");
        String filename = safeName + "_" + difficulty + ".txt";
        
        String line = name + ";;;" + difficulty + ";;;" + filename;
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUIZ_LIST_FILE, true))) {
            bw.write(line);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Quiz olusturulamadi: " + e.getMessage());
        }
    }

    public static void loadQuestionsForQuiz(Quiz quiz) {
        File file = new File(quiz.getFilename());
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";;;");
                
                String type = parts[0];
                String text = parts[1];
                int points = Integer.parseInt(parts[2]);

                if (type.equals("MCQ")) {
                    String[] optionsArr = parts[3].split(",");
                    ArrayList<String> options = new ArrayList<>();
                    for (String opt : optionsArr) options.add(opt);
                    int correctIndex = Integer.parseInt(parts[4]);
                    
                    quiz.addQuestion(new MultipleChoiceQuestion(text, points, options, correctIndex));

                } else if (type.equals("TF")) {
                    boolean answer = Boolean.parseBoolean(parts[3]);
                    quiz.addQuestion(new TrueFalseQuestion(text, points, answer));
                }
            }
        } catch (IOException e) {
            System.out.println("Sorular yuklenemedi: " + e.getMessage());
        }
    }

    public static void addLineToQuizFile(String filename, String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Soru kaydedilemedi: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadRawLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) {}
        return lines;
    }

    public static void rewriteQuizFile(String filename, ArrayList<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {}
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("-> Quiz dosyasi (" + filename + ") silindi.");
            } else {
                System.out.println("-> HATA: Quiz dosyasi silinemedi.");
            }
        }
    }

    public static void updateQuizListFile(ArrayList<Quiz> quizzes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(QUIZ_LIST_FILE, false))) {
            for (Quiz q : quizzes) {
                String line = q.getName() + ";;;" + q.getDifficulty() + ";;;" + q.getFilename();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Quiz listesi guncellenemedi: " + e.getMessage());
        }
    }
}
