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
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.singletons.CurrentUser;

// Custom class for dialog fragment used to create new course, user must fill the title and
// can fill the description for the course, course is saved in the database
public class AddCourseDialogFragment extends DialogFragment
{
    private static final String TAG = "ADD_COURSE_DIALOG_DEBUG";

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private final CollectionReference coursesRef = database.collection("Courses");

    private EditText courseNameEditText;
    private EditText courseDescriptionEditText;
    private Button addButton;
    private Button cancelButton;

    private Context applicationContext;

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
        addButton = view.findViewById(R.id.add_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        applicationContext = getActivity().getApplicationContext();

        addButton.setOnClickListener(v -> {
            String newCourseName = courseNameEditText.getText().toString().trim();
            String newCourseDescription = courseDescriptionEditText.getText().toString().trim();
            if(!newCourseName.isEmpty())
            {
                coursesRef.document(newCourseName).set(new Course(newCourseName,
                        CurrentUser.getCurrentUser().getName(), newCourseDescription))
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(applicationContext, R.string.create_course_success, Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(applicationContext, R.string.create_course_failed, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Couldn't add new course\n" + e.toString());
                        });
                getDialog().dismiss();
            } else
            {
                courseNameEditText.setError("Course must have a name");
                courseNameEditText.requestFocus();
            }
        });
        cancelButton.setOnClickListener(v -> getDialog().dismiss());

        return builder.create();
    }
}
