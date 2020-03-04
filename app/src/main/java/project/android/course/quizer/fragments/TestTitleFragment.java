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

public class TestTitleFragment extends Fragment
{
    private static final String TAG = "ADDING_NEW_TEST_DEBUG";
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
        nextButton =  view.findViewById(R.id.button_save);
        testNameEditText = view.findViewById(R.id.edit_text_test_name);
        dueDateEditText = view.findViewById(R.id.edit_text_test_due_date);
        numOfQuestionsEditText = view.findViewById(R.id.edit_text_num_of_questions);
        parentActivity = (CreateTestActivity) getActivity();

        if(!parentActivity.getQuestions().isEmpty())
            nextButton.setText(R.string.button_edit);

        nextButton.setOnClickListener(v -> {
            String testName = testNameEditText.getText().toString().trim();
            String numOfQuestions = numOfQuestionsEditText.getText().toString().trim();
            Date dueDate = null;
            try
            {
                dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateEditText.getText().toString().trim());
            } catch(ParseException e)
            {
                Log.d(TAG, "onCreateView: Couldn't format the date\n" + e.toString());
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

            parentActivity.addQuestionFragment();
            parentActivity.moveToNextPage();
            nextButton.setText(R.string.button_edit);
        });

        return view;
    }
}
