package project.android.course.quizer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.singletons.CurrentUser;

public class AddCourseDialogFragment extends DialogFragment
{
    private static final String TAG = "ADD_COURSE_DIALOG_DEBUG";
    private FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private final CollectionReference coursesRef = mDatabase.collection("Courses");
    private EditText courseNameEditText;
    private EditText courseDescriptionEditText;
    private Button addButton;
    private Button cancelButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_course_dialog, null);
        builder.setView(view);

        courseNameEditText = view.findViewById(R.id.course_name_edit_text);
        courseDescriptionEditText = view.findViewById(R.id.course_description_edit_text);
        addButton = view.findViewById(R.id.button_add);
        cancelButton = view.findViewById(R.id.button_cancel);

        addButton.setOnClickListener(v -> {
            String newCourseName = courseNameEditText.getText().toString().trim();
            String newCourseDescription = courseDescriptionEditText.getText().toString().trim();
            if(!newCourseName.isEmpty())
            {
                coursesRef.document(newCourseName).set(new Course(newCourseName,
                        CurrentUser.getCurrentUser().getName(),newCourseDescription))
                        .addOnSuccessListener(aVoid ->
                                Log.d(TAG, "Successfully added new course"))
                        .addOnFailureListener(e ->
                                Log.d(TAG, "Couldn't add new course\n" + e.toString()));
                getDialog().dismiss();
            }
            else
            {
                courseNameEditText.setError("Course must have a name");
                courseNameEditText.requestFocus();
            }
        });
        cancelButton.setOnClickListener(v -> getDialog().dismiss());

        return builder.create();
    }
}
//TODO: Naprawić wyświetlanie testów do uzupełnienia
//TODO: Zrobić edycję kursów dla nauczyciela
//Todo: Zrobić dodawanie pytań do testu dla nauczyciela, wtedy trzeba wybrać kilka z pytań do odrzucenia przed testem
