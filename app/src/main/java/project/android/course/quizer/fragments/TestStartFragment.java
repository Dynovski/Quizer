package project.android.course.quizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;

// Fragment displaying basic information about the test and providing action to start the test
public class TestStartFragment extends Fragment
{
    private TextView testNameTextView;
    private TextView numOfQuestionsTextView;
    private TextView deadlineTextView;
    private Button beginTestButton;

    private SolveTestActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_solve_test_start, container, false);

        testNameTextView = view.findViewById(R.id.test_name_text_view);
        numOfQuestionsTextView = view.findViewById(R.id.num_of_questions_text_view);
        deadlineTextView = view.findViewById(R.id.deadline_text_view);

        beginTestButton = view.findViewById(R.id.begin_test_button);

        parentActivity = (SolveTestActivity) getActivity();

        testNameTextView.setText(parentActivity.getTestName());
        numOfQuestionsTextView.setText(Integer.toString(parentActivity.getNumOfQuestions()));
        deadlineTextView.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(parentActivity.getDeadline().toDate()));

        if(parentActivity.testStarted())
        {
            beginTestButton.setOnClickListener(null);
            beginTestButton.setVisibility(View.INVISIBLE);
        } else
        {
            beginTestButton.setOnClickListener(v -> {
                parentActivity.addQuestions();
                parentActivity.moveToNextPage();
            });
        }
        return view;
    }
}
