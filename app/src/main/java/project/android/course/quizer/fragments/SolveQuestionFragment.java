package project.android.course.quizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;

public class SolveQuestionFragment extends Fragment
{
    private TextView questionTextView;
    private RadioButton answer1;
    private RadioButton answer2;
    private RadioButton answer3;
    private RadioButton answer4;
    private Button saveButton;
    private SolveTestActivity parentActivity;
    private int questionId;

    public SolveQuestionFragment(int questionId)
    {
        this.questionId = questionId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_solve_test_question, container, false);
        questionTextView = view.findViewById(R.id.text_view_question);
        answer1 = view.findViewById(R.id.radio_button_answer1);
        answer2 = view.findViewById(R.id.radio_button_answer2);
        answer3 = view.findViewById(R.id.radio_button_answer3);
        answer4 = view.findViewById(R.id.radio_button_answer4);
        saveButton = view.findViewById(R.id.button_save);
        parentActivity = (SolveTestActivity) getActivity();

        questionTextView.setText(parentActivity.getQuestions().get(questionId).getQuestionText());
        answer1.setText(parentActivity.getAnswers().get(4 * questionId).getAnswerText());
        answer2.setText(parentActivity.getAnswers().get(4 * questionId + 1).getAnswerText());
        answer3.setText(parentActivity.getAnswers().get(4 * questionId + 2).getAnswerText());
        answer4.setText(parentActivity.getAnswers().get(4 * questionId + 3).getAnswerText());

        // When at the last question
        if(parentActivity.getNumOfQuestions() == questionId + 1)
        {
            saveButton.setOnClickListener(v -> {
                parentActivity.checkTest();
                parentActivity.addSummaryFragment();
            });
        } else
        {
            saveButton.setOnClickListener(null);
            saveButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public boolean answersCorrect()
    {
        boolean firstAnswerCorrect = parentActivity.getAnswers().get(4 * questionId).isCorrect() == answer1.isChecked();
        boolean secondAnswerCorrect = parentActivity.getAnswers().get(4 * questionId + 1).isCorrect() == answer2.isChecked();
        boolean thirdAnswerCorrect = parentActivity.getAnswers().get(4 * questionId + 2).isCorrect() == answer3.isChecked();
        boolean fourthAnswerCorrect = parentActivity.getAnswers().get(4 * questionId + 3).isCorrect() == answer4.isChecked();

        return firstAnswerCorrect && secondAnswerCorrect && thirdAnswerCorrect && fourthAnswerCorrect;
    }

    public int getQuestionId()
    {
        return questionId;
    }
}
