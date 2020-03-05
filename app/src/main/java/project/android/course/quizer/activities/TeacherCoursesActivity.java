package project.android.course.quizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.viewmodels.TeacherCoursesViewModel;

// Activity coordinating the display of the list of courses of a given teacher, its recyclerView's cells
// recyclerView operates on LiveData, so it is updated automatically when changes in database occur
public class TeacherCoursesActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    private static final String TAG = "TEACHER_COURSES_DEBUG";
    private TeacherCoursesViewModel teacherCoursesViewModel;

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teacherCoursesViewModel = new ViewModelProvider(this).get(TeacherCoursesViewModel.class);
        teacherCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setCourses(queryDocumentSnapshots));
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.contextual_menu_teacher);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.edit_course_item:
                return super.onContextItemSelected(item);
            case R.id.delete_course_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to delete this course?");
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(adapter.getSelectedCourse().getCourseName()).delete()
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully deleted course"))
                            .addOnFailureListener(e -> Log.d(TAG, "Couldn't delete course\n" + e.toString()));
                });
                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.add_test_item:
                Intent testIntent = new Intent(this, CreateTestActivity.class);
                testIntent.putExtra("COURSENAME", adapter.getSelectedCourse().getCourseName());
                startActivity(testIntent);
            default:
                return super.onContextItemSelected(item);
        }
    }
}
