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
        } catch (IOException e) {}
        return quizzes;
    }

    public static void loadQuestonsForQuiz (Quiz quiz) {

    }
}
