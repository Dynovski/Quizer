package project.android.course.quizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.CreateTestActivity;
import project.android.course.quizer.firebaseObjects.Answer;
import project.android.course.quizer.firebaseObjects.Question;

// Fragment used to enter data to create one question and its answers, there are four answers and
// at least one of them must be correct it is impossible to leave blank question or answers
public class CreateQuestionFragment extends Fragment
{
    private EditText questionEditText;
    private Button nextButton;

    private EditText answer1EditText;
    private EditText answer2EditText;
    private EditText answer3EditText;
    private EditText answer4EditText;

    private CheckBox answer1CheckBox;
    private CheckBox answer2CheckBox;
    private CheckBox answer3CheckBox;
    private CheckBox answer4CheckBox;

    private CreateTestActivity parentActivity;

    private int questionId;

    public CreateQuestionFragment(int questionId)
    {
        this.questionId = questionId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_test_question, container, false);

        nextButton = view.findViewById(R.id.save_button);

        questionEditText = view.findViewById(R.id.question_edit_text);

        answer1EditText = view.findViewById(R.id.answer_1_edit_text);
        answer2EditText = view.findViewById(R.id.answer_2_edit_text);
        answer3EditText = view.findViewById(R.id.answer_3_edit_text);
        answer4EditText = view.findViewById(R.id.answer_4_edit_text);

        answer1CheckBox = view.findViewById(R.id.check_box_answer1);
        answer2CheckBox = view.findViewById(R.id.check_box_answer2);
        answer3CheckBox = view.findViewById(R.id.check_box_answer3);
        answer4CheckBox = view.findViewById(R.id.check_box_answer4);

        parentActivity = (CreateTestActivity) getActivity();

        // If this fragment is not the last one
        if(parentActivity.getQuestions().size() > questionId + 1)
            nextButton.setText(R.string.edit);

        // If this fragment is final one
        if(parentActivity.getTest().getNumOfQuestions() == questionId + 1)
            nextButton.setText(R.string.create_test);

        nextButton.setOnClickListener(v -> {
            String question = questionEditText.getText().toString().trim();
            if(question.isEmpty())
            {
                questionEditText.setError("Question cannot be empty");
                questionEditText.requestFocus();
                return;
            }

            String answer1 = answer1EditText.getText().toString().trim();
            String answer2 = answer2EditText.getText().toString().trim();
            String answer3 = answer3EditText.getText().toString().trim();
            String answer4 = answer4EditText.getText().toString().trim();

            boolean true1 = answer1CheckBox.isChecked();
            boolean true2 = answer2CheckBox.isChecked();
            boolean true3 = answer3CheckBox.isChecked();
            boolean true4 = answer4CheckBox.isChecked();

            if(answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty())
            {
                Toast.makeText(parentActivity, "Answers cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(true1 || true2 || true3 || true4))
            {
                Toast.makeText(parentActivity, "At least one answer must be correct", Toast.LENGTH_SHORT).show();
                return;
            }

            parentActivity.setQuestion(new Question(question), questionId);

            Answer[] answers = new Answer[]{new Answer(answer1, true1), new Answer(answer2, true2),
                    new Answer(answer3, true3), new Answer(answer4, true4)};
            parentActivity.setAnswers(answers, questionId);

            // If final fragment save test in database
            if(parentActivity.getTest().getNumOfQuestions() == questionId + 1)
                parentActivity.saveTest();
            else
            {
                // Skip if not currently last (not to add unnecessary fragments)
                if(!(parentActivity.getQuestions().size() - 1 > questionId))
                {
                    parentActivity.addQuestionFragment();
                    parentActivity.moveToNextPage();
                    nextButton.setText(R.string.edit);
                }
            }
        });
        return view;
    }
}
