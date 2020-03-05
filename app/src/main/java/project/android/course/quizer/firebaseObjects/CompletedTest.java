package project.android.course.quizer.firebaseObjects;

import com.google.firebase.Timestamp;

public class CompletedTest
{
    private String testName;
    private String courseName;
    private int numberOfQuestions;
    private int score;
    private Timestamp finishDate;

    CompletedTest() {}

    public CompletedTest(String testName, String courseName, int numberOfQuestions, int score, Timestamp finishDate)
    {
        this.testName = testName;
        this.courseName = courseName;
        this.numberOfQuestions = numberOfQuestions;
        this.score = score;
        this.finishDate = finishDate;
    }

    public String getTestName()
    {
        return testName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public int getNumberOfQuestions()
    {
        return numberOfQuestions;
    }

    public int getScore()
    {
        return score;
    }

    public Timestamp getFinishDate()
    {
        return finishDate;
    }
}
