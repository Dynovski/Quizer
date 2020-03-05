package project.android.course.quizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;

public class TestSummaryFragment extends Fragment
{
    private TextView testNameTextView;
    private TextView scoreTextView;
    private Button endButton;
    private SolveTestActivity parentActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_test_summary, container, false);
        testNameTextView = view.findViewById(R.id.test_name_tv);
        scoreTextView = view.findViewById(R.id.score_tv);
        endButton = view.findViewById(R.id.button_end);
        parentActivity = (SolveTestActivity) getActivity();

        testNameTextView.setText(parentActivity.getTestName());
        scoreTextView.setText(parentActivity.getTestScore());

        endButton.setOnClickListener(v -> parentActivity.finish());

        return view;
    }


}
