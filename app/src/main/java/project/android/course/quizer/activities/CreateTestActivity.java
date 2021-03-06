package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.ViewPagerAdapter;
import project.android.course.quizer.firebaseObjects.Answer;
import project.android.course.quizer.firebaseObjects.Question;
import project.android.course.quizer.firebaseObjects.Test;
import project.android.course.quizer.fragments.CreateQuestionFragment;
import project.android.course.quizer.fragments.TestTitleFragment;

// Activity coordinating test creation, it adds new fragments to the view pager, provides method to
// navigate in view pager, stores data to write to database and executes write
public class CreateTestActivity extends AppCompatActivity
{
    private static final String TAG = "CREATE_TEST_DEBUG";

    // Firebase variables
    private FirebaseFirestore database;
    private CollectionReference testsRef;

    // Layout related variables
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    // Variables holding data to save
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private Test test;

    // Variable to manage arrayList operations
    private int questionCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        database = FirebaseFirestore.getInstance();
        testsRef = database.collection("Tests");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String courseName = getIntent().getExtras().getString("courseName");
        adapter.addFragment(new TestTitleFragment(courseName), "Choose test name");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public ArrayList<Question> getQuestions()
    {
        return questions;
    }

    public void setQuestion(Question question, int questionId)
    {
        this.questions.set(questionId, question);
    }

    public ArrayList<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers(Answer[] answers, int questionId)
    {
        for(int i = 0; i < answers.length; i++)
            this.answers.set(4 * questionId + i, answers[i]);
    }

    public Test getTest()
    {
        return test;
    }

    public void setTest(Test test)
    {
        this.test = test;
    }

    public void addQuestionFragment()
    {
        adapter.addFragment(new CreateQuestionFragment(questionCount++), "Question " + questionCount);
        adapter.notifyDataSetChanged();
        questions.add(null);
        for(int i = 0; i < 4; i++)
            answers.add(null);
    }

    public void saveTest()
    {
        executeBatchedWrite(getApplicationContext());
        finish();
    }

    public void moveToNextPage()
    {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private void executeBatchedWrite(Context context)
    {
        WriteBatch batch = database.batch();
        DocumentReference newTestDocument = testsRef.document(test.getTestName());
        batch.set(newTestDocument, test);
        CollectionReference testQuestionsRef = newTestDocument.collection("Questions");
        int i = 0;
        for(Question question : questions)
        {
            DocumentReference newQuestionDocument = testQuestionsRef.document();
            batch.set(newQuestionDocument, question);
            CollectionReference questionAnswersRef = newQuestionDocument.collection("Answers");
            for(int j = 4 * i; j < 4 * i + 4; j++)
            {
                DocumentReference newAnswerDocument = questionAnswersRef.document();
                batch.set(newAnswerDocument, answers.get(j));
            }
            ++i;
        }
        batch.commit()
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(context, "Successfully added new test", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Couldn't add new test", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Adding new test failed\n" + e.toString());
                });
    }
}
