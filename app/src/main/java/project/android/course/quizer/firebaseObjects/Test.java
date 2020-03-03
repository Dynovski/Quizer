package project.android.course.quizer.firebaseObjects;

import com.google.firebase.Timestamp;

public class Test
{
    private String testName;
    private Timestamp dueDate;
    private int numOfQuestions;

    public Test() {}

    public Test(String testName, Timestamp dueDate, int numOfQuestions)
    {
        this.testName = testName;
        this.dueDate = dueDate;
        this.numOfQuestions = numOfQuestions;
    }

    public String getTestName()
    {
        return testName;
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
