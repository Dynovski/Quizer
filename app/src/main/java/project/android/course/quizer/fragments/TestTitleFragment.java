package project.android.course.quizer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.CreateTestActivity;
import project.android.course.quizer.firebaseObjects.Test;

// Fragment for adding basic data about the test to save it in database upon test creation
public class TestTitleFragment extends Fragment
{
    private static final String TAG = "TEST_START_DEBUG";

    private Button nextButton;
    private EditText testNameEditText;
    private EditText dueDateEditText;
    private EditText numOfQuestionsEditText;

    private CreateTestActivity parentActivity;

    private String courseName;

    public TestTitleFragment(String courseName)
    {
        this.courseName = courseName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_test_name, container, false);

        nextButton = view.findViewById(R.id.save_button);
        testNameEditText = view.findViewById(R.id.test_name_edit_text);
        dueDateEditText = view.findViewById(R.id.test_deadline_edit_text);
        numOfQuestionsEditText = view.findViewById(R.id.num_of_questions_edit_text);

        parentActivity = (CreateTestActivity) getActivity();

        // If question adding started
        if(!parentActivity.getQuestions().isEmpty())
            nextButton.setText(R.string.edit);

        nextButton.setOnClickListener(v -> {
            String testName = testNameEditText.getText().toString().trim();
            String numOfQuestions = numOfQuestionsEditText.getText().toString().trim();

            Date dueDate = null;
            try
            {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateEditText.getText().toString().trim());
            } catch(ParseException e)
            {
                Log.d(TAG, "Couldn't format the date\n" + e.toString());
            }

            Date finalDueDate = dueDate;
            if(testName.isEmpty())
            {
                testNameEditText.setError("Test must have a name");
                testNameEditText.requestFocus();
                return;
            }

            if(dueDateEditText.getText().toString().trim().isEmpty())
            {
                dueDateEditText.setError("Test must have a deadline");
                dueDateEditText.requestFocus();
                return;
            }

            if(numOfQuestions.isEmpty())
            {
                numOfQuestionsEditText.setError("Choose number of test questions");
                numOfQuestionsEditText.requestFocus();
                return;
            }

            parentActivity.setTest(new Test(testNameEditText.getText().toString().trim(), courseName,
                    new Timestamp(finalDueDate), Integer.parseInt(numOfQuestions)));

            if(parentActivity.getQuestions().isEmpty())
            {
                parentActivity.addQuestionFragment();
                parentActivity.moveToNextPage();
                nextButton.setText(R.string.edit);
            }
        });
        return view;
    }
}
