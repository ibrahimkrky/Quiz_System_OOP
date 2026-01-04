import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class QuizTest {

    @Test
    public void testMultipleChoiceCorrectAnswer() {
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        options.add("C");
        options.add("D");

        MultipleChoiceQuestion q = new MultipleChoiceQuestion("Test Sorusu", 10, options, 1);

        assertTrue("Dogru sik secildiginde true donmeli", q.checkAnswer("2"));
    }

    @Test
    public void testMultipleChoiceWrongAnswer() {
        ArrayList<String> options = new ArrayList<>();
        options.add("A");
        options.add("B");
        
        MultipleChoiceQuestion q = new MultipleChoiceQuestion("Test Sorusu", 10, options, 0);

        assertFalse("Yanlis sik secildiginde false donmeli", q.checkAnswer("2"));
    }

    @Test
    public void testTrueFalseQuestion() {
        TrueFalseQuestion q = new TrueFalseQuestion("Java guzeldir", 5, true);

        assertTrue("true cevabi dogru kabul edilmeli", q.checkAnswer("true"));
        assertTrue("TRUE cevabi (buyuk harf) dogru kabul edilmeli", q.checkAnswer("TRUE"));
        assertFalse("false cevabi yanlis kabul edilmeli", q.checkAnswer("false"));
    }

    @Test
    public void testStudentScore() {
        Student student = new Student("Ali", "123");
        
        assertEquals(0, student.getScore());

        student.addScore(10);
        assertEquals(10, student.getScore());

        student.addScore(5);
        assertEquals(15, student.getScore());
    }

    @Test
    public void testQuizAddQuestion() {
        Quiz quiz = new Quiz("Test Quiz", Difficulty.Kolay, "test.txt");
        
        assertTrue(quiz.getQuestions().isEmpty());

        TrueFalseQuestion q = new TrueFalseQuestion("Soru", 10, true);
        quiz.addQuestion(q);

        assertEquals(1, quiz.getQuestions().size());
        
        assertEquals("Soru", quiz.getQuestions().get(0).getText());
    }
}
