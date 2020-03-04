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

import java.util.ArrayList;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;
import project.android.course.quizer.firebaseObjects.Answer;
import project.android.course.quizer.firebaseObjects.Question;

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
        questionId = questionId;
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

        ArrayList<Question> questions = parentActivity.getQuestions();
        ArrayList<Answer> answers = parentActivity.getAnswers();

        questionTextView.setText(parentActivity.getQuestions().get(questionId).getQuestionText());
        answer1.setText(parentActivity.getAnswers().get(4 * questionId).getAnswerText());
        answer2.setText(parentActivity.getAnswers().get(4 * questionId + 1).getAnswerText());
        answer3.setText(parentActivity.getAnswers().get(4 * questionId + 2).getAnswerText());
        answer4.setText(parentActivity.getAnswers().get(4 * questionId + 3).getAnswerText());

        // When at the last question
        if(parentActivity.getNumOfQuestions() == questionId + 1)
        {
            saveButton.setOnClickListener(v -> {
                parentActivity.finish();
                //TODO: zebrac dane i zapisac wynik, test umescic w wykonanych testach
                //TODO: dialog czy napewno chce skonczyc
            });
        }
        else
        {
            saveButton.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}