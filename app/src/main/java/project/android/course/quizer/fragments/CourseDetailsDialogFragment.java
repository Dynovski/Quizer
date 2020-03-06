package project.android.course.quizer.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;

public class CourseDetailsDialogFragment extends DialogFragment
{
    private TextView courseNameTextView;
    private TextView teacherNameTextView;
    private TextView courseDescriptionTextView;
    private Course displayedCourse;

    public CourseDetailsDialogFragment(Course displayedCourse)
    {
        this.displayedCourse = displayedCourse;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_course_details_dialog, null);
        builder.setView(view);

        courseNameTextView = view.findViewById(R.id.text_view_course_name);
        teacherNameTextView = view.findViewById(R.id.text_view_teacher_name);
        courseDescriptionTextView = view.findViewById(R.id.text_view_course_description);

        courseNameTextView.setText(displayedCourse.getCourseName());
        teacherNameTextView.setText(displayedCourse.getTeacher());
        courseNameTextView.setText(displayedCourse.getDescription());

        return builder.create();
    }
}
