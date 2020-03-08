package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.ViewPagerAdapter;
import project.android.course.quizer.firebaseObjects.Answer;
import project.android.course.quizer.firebaseObjects.CompletedTest;
import project.android.course.quizer.firebaseObjects.Question;
import project.android.course.quizer.fragments.SolveQuestionFragment;
import project.android.course.quizer.fragments.TestStartFragment;
import project.android.course.quizer.fragments.TestSummaryFragment;

// Activity coordinating test solving display, it loads data from database to RAM and displays it
// using view pager, user can therefore go back to the previous questions and change answers before
// finishing test, then in checks answers and displays result in summary fragment
public class SolveTestActivity extends AppCompatActivity
{
    private static final String TAG = "DATABASE_TEST_SOLVING";

    // Firebase variables
    private FirebaseFirestore database;
    private CollectionReference testsRef;
    private CollectionReference resultsRef;
    private CollectionReference completedTestsRef;

    // Layout related variables
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    // Variables holding data to display
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;

    // Help variables
    private String testName;
    private String courseName;
    private int numOfQuestions;
    private Timestamp deadline;
    private int testScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        database = FirebaseFirestore.getInstance();
        testsRef = database.collection("Tests");
        resultsRef = database.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Results");
        completedTestsRef = database.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("CompletedTests");

        Bundle receivedBundle = getIntent().getExtras();
        assert(receivedBundle != null);

        testName = receivedBundle.getString("testName");
        courseName = receivedBundle.getString("courseName");
        numOfQuestions = receivedBundle.getInt("numOfQuestions");
        deadline = (Timestamp) receivedBundle.get("deadline");

        // Creating ArrayLists with specific size to make it possible for asynchronous queries
        // to safely run without worrying about getting results in random order
        questions = new ArrayList<>(Arrays.asList(new Question[numOfQuestions]));
        answers = new ArrayList<>(Arrays.asList(new Answer[4 * numOfQuestions]));

        getQuestionsAndAnswersFromDatabase();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TestStartFragment(), "Test information");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getQuestionsAndAnswersFromDatabase()
    {
        testsRef.document(testName).collection("Questions").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int i = 0;
                    for(DocumentSnapshot questionDocument : queryDocumentSnapshots.getDocuments())
                    {
                        questions.set(i, questionDocument.toObject(Question.class));
                        int finalI = i;
                        questionDocument.getReference().collection("Answers").get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    int j = 0;
                                    for(DocumentSnapshot answerDocument : queryDocumentSnapshots1.getDocuments())
                                    {
                                        answers.set(4 * finalI + j++, answerDocument.toObject(Answer.class));
                                    }
                                    Log.d(TAG, "Successfully retrieved answers for question " + finalI);
                                })
                                .addOnFailureListener(e ->
                                        Log.d(TAG, "Couldn't retrieve answers for question " + finalI + "\n"
                                        + e.toString()));
                        i++;
                    }
                    Log.d(TAG, "Successfully retrieved all questions and answers");
                })
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't retrieve questions and answers\n"
                        + e.toString()));
    }

    public void moveToNextPage()
    {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void addQuestions()
    {
        for(int i = 0; i < numOfQuestions;)
        {
            adapter.addFragment(new SolveQuestionFragment(i++), "Question " + i);
        }
        adapter.notifyDataSetChanged();
    }

    public void checkTest()
    {
        for(int i = 1; i < adapter.getCount(); i++)
        {
            if(((SolveQuestionFragment)adapter.getItem(i)).answersCorrect())
                ++testScore;
        }

        completedTestsRef.document().set(new CompletedTest(testName, courseName, numOfQuestions, testScore, new Timestamp(new Date())))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully added completed test"))
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't add completed test\n" + e.toString()));
    }

    public void addSummaryFragment()
    {
        adapter.addFragment(new TestSummaryFragment(), "Test Summary");
        adapter.notifyDataSetChanged();
        moveToNextPage();
    }

    public boolean testStarted()
    {
        return adapter.getCount() > 1;
    }

    public String getTestName()
    {
        return testName;
    }

    public int getNumOfQuestions()
    {
        return numOfQuestions;
    }

    public Timestamp getDeadline()
    {
        return deadline;
    }

    public ArrayList<Question> getQuestions()
    {
        return questions;
    }

    public ArrayList<Answer> getAnswers()
    {
        return answers;
    }

    public int getTestScore()
    {
        return testScore;
    }
}
