package project.android.course.quizer.firebaseObjects;

public class CompletedTest
{
    private String testName;
    private int numberOfQuestions;
    private int score;

    CompletedTest() {}

    public CompletedTest(String testName, int numberOfQuestions, int score)
    {
        this.testName = testName;
        this.numberOfQuestions = numberOfQuestions;
        this.score = score;
    }

    public String getTestName()
    {
        return testName;
    }

    public int getNumberOfQuestions()
    {
        return numberOfQuestions;
    }

    public int getScore()
    {
        return score;
    }
}
