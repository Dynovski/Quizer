package project.android.course.quizer.firebaseObjects;

// Conversion class for answers from database to java code and the other way
public class Answer
{
    private String answerText;
    private boolean correct;

    // Empty constructor needed by Firebase
    public Answer() {}

    public Answer(String answerText, boolean correct)
    {
        this.answerText = answerText;
        this.correct = correct;
    }

    public String getAnswerText()
    {
        return answerText;
    }

    public boolean isCorrect()
    {
        return correct;
    }
}
