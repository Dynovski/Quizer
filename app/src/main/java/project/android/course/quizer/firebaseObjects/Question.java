package project.android.course.quizer.firebaseObjects;

import java.util.ArrayList;
import java.util.Map;

public class Question
{
    private String questionText;

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
