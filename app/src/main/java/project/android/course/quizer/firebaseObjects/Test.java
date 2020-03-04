package project.android.course.quizer.firebaseObjects;

import com.google.firebase.Timestamp;

public class Test
{
    private String testName;
    private String courseName;
    private Timestamp dueDate;
    private int numOfQuestions;

    public Test() {}

    public Test(String testName, String courseName, Timestamp dueDate, int numOfQuestions)
    {
        this.testName = testName;
        this.courseName = courseName;
        this.dueDate = dueDate;
        this.numOfQuestions = numOfQuestions;
    }

    public String getTestName()
    {
        return testName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public Timestamp getDueDate()
    {
        return dueDate;
    }

    public int getNumOfQuestions()
    {
        return numOfQuestions;
    }
}
