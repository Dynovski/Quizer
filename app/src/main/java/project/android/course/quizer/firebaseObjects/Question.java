package project.android.course.quizer.firebaseObjects;

// Conversion class for questions from database to java code and the other way
public class Question
{
    private String questionText;

    // Empty constructor needed by Firebase
    public Question() {}

    public Question(String questionText)
    {
        this.questionText = questionText;
    }

    public String getQuestionText()
    {
        return questionText;
    }
}
