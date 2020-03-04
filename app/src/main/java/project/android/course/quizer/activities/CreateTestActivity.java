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
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.firebaseObjects.Question;
import project.android.course.quizer.firebaseObjects.Test;
import project.android.course.quizer.fragments.QuestionFragment;
import project.android.course.quizer.fragments.TestTitleFragment;
import project.android.course.quizer.singletons.CurrentUser;

public class CreateTestActivity extends AppCompatActivity
{
    private static final String TAG = "DATABASE_TEST_ADDING";
    private FirebaseFirestore mDatabase;
    private CollectionReference testsRef;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private Test test;
    private ViewPagerAdapter adapter;
    private int questionCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        mDatabase = FirebaseFirestore.getInstance();
        // TODO: ZMIENIĆ ZEBY KURS NIE BYŁ HARDCODED
        testsRef = mDatabase.collection("Courses").document("Logic")
                .collection("Tests");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TestTitleFragment(), "Choose test name");
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
        adapter.addFragment(new QuestionFragment(questionCount++), "Question " + questionCount);
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

    private void executeBatchedWrite(Context context)
    {
        WriteBatch batch = mDatabase.batch();
        DocumentReference newTestDocument = testsRef.document();
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
                    Log.d(TAG, e.toString());
                });
    }

    public void moveToNextPage()
    {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }
}
