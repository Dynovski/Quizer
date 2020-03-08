package project.android.course.quizer.firebaseObjects;

import com.google.firebase.Timestamp;

// Conversion class for tests from database to java code and the other way
public class Test
{
    private String testName;
    private String courseName;
    private Timestamp dueDate;
    private int numOfQuestions;

    // Empty constructor needed by Firebase
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
