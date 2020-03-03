//package project.android.course.quizer.fragments;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//
//import project.android.course.quizer.R;
//import project.android.course.quizer.activities.CreateTestActivity;
//import project.android.course.quizer.firebaseObjects.Answer;
//import project.android.course.quizer.firebaseObjects.Question;
//
//public class AddAnswersDialogFragment extends DialogFragment
//{
//    private static final String TAG = "ADD_ANSWERS_DIALOG_DEBUG";
//    private EditText answer1EditText;
//    private EditText answer2EditText;
//    private EditText answer3EditText;
//    private EditText answer4EditText;
//    private CheckBox answer1CheckBox;
//    private CheckBox answer2CheckBox;
//    private CheckBox answer3CheckBox;
//    private CheckBox answer4CheckBox;
//    private Button addButton;
//    private Button cancelButton;
//    private CreateTestActivity parentActivity;
//    private String question;
//
//    public AddAnswersDialogFragment(CreateTestActivity parent, String question)
//    {
//        super();
//        this.parentActivity = parent;
//        this.question = question;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//        LayoutInflater inflater = parentActivity.getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_create_test_answers_dialog, null);
//        builder.setView(view);
//
//        answer1EditText = view.findViewById(R.id.edit_text_answer1);
//        answer2EditText = view.findViewById(R.id.edit_text_answer2);
//        answer3EditText = view.findViewById(R.id.edit_text_answer3);
//        answer4EditText = view.findViewById(R.id.edit_text_answer4);
//
//        answer1CheckBox = view.findViewById(R.id.check_box_answer1);
//        answer2CheckBox = view.findViewById(R.id.check_box_answer2);
//        answer3CheckBox = view.findViewById(R.id.check_box_answer3);
//        answer4CheckBox = view.findViewById(R.id.check_box_answer4);
//
//        addButton = view.findViewById(R.id.button_add);
//        cancelButton = view.findViewById(R.id.button_cancel);
//
//        addButton.setOnClickListener(v -> {
//
//            String answer1 = answer1EditText.getText().toString();
//            String answer2 = answer2EditText.getText().toString();
//            String answer3 = answer3EditText.getText().toString();
//            String answer4 = answer4EditText.getText().toString();
//
//            boolean true1 = answer1CheckBox.isChecked();
//            boolean true2 = answer2CheckBox.isChecked();
//            boolean true3 = answer3CheckBox.isChecked();
//            boolean true4 = answer4CheckBox.isChecked();
//
//            if(answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty())
//            {
//                Toast.makeText(parentActivity, "Answers cannot be empty", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if(!(true1 || true2 || true3 || true4))
//            {
//                Toast.makeText(parentActivity, "At least one answer must be correct", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            parentActivity.addQuestion(new Question(question));
//            parentActivity.addAnswer(new Answer(answer1, true1));
//            parentActivity.addAnswer(new Answer(answer2, true2));
//            parentActivity.addAnswer(new Answer(answer3, true3));
//            parentActivity.addAnswer(new Answer(answer4, true4));
//            //TODO: ZEBY DZIALALO Z WIELOKROTNYM PRZYCISNIECIEM
//            getDialog().dismiss();
//        });
//        cancelButton.setOnClickListener(v -> getDialog().dismiss());
//        return builder.create();
//    }
//}
