package project.android.course.quizer.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
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
    private final CollectionReference coursesCollectionRef = mDatabase.collection("Courses");
    private EditText courseNameEditText;
    private Button addButton;
    private Button cancelButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_course_dialog, null);
        builder.setView(view);

        courseNameEditText = view.findViewById(R.id.edit_text_new_course);
        addButton = view.findViewById(R.id.button_add);
        cancelButton = view.findViewById(R.id.button_cancel);

        addButton.setOnClickListener(v -> {
            String newCourseName = courseNameEditText.getText().toString();
            if(!newCourseName.isEmpty())
            {
                executeBatchedWrite(newCourseName, getContext());
                getDialog().dismiss();
            }
        });
        cancelButton.setOnClickListener(v -> getDialog().dismiss());

        return builder.create();
    }

    private void executeBatchedWrite(String newCourseName, Context context)
    {
        WriteBatch batch = mDatabase.batch();
        DocumentReference newCourse = coursesCollectionRef.document();
        batch.set(newCourse, new Course(newCourseName, CurrentUser.getCurrentUser().getName()));
        batch.commit()
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(context, "Successfully added new course", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
            Toast.makeText(context, "Couldn't add new course", Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.toString());
        });
    }
}
