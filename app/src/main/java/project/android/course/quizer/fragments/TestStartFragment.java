package project.android.course.quizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;

public class TestStartFragment extends Fragment
{
    private static final String TAG = "SOLVE_TEST_INFO_FRAGMENT";
    private TextView testNameTextView;
    private TextView numOfQuestionsTextView;
    private TextView deadlineTextView;
    private Button beginTestButton;
    private SolveTestActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_solve_test_start, container, false);
        testNameTextView =  view.findViewById(R.id.text_view_test_name);
        numOfQuestionsTextView = view.findViewById(R.id.text_view_num_of_questions);
        deadlineTextView = view.findViewById(R.id.text_view_deadline);
        beginTestButton = view.findViewById(R.id.button_begin_test);
        parentActivity = (SolveTestActivity) getActivity();

        testNameTextView.setText(parentActivity.getTestName());
        numOfQuestionsTextView.setText(Integer.toString(parentActivity.getNumOfQuestions()));
        deadlineTextView.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(parentActivity.getDeadline().toDate()));

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
