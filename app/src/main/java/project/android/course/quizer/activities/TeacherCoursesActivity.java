package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.viewmodels.TeacherCoursesViewModel;

// Activity coordinating the display of the list of courses of a given teacher, its recyclerView's cells
// recyclerView operates on LiveData, so it is updated automatically when changes in database occur
public class TeacherCoursesActivity extends AppCompatActivity
{
    private TeacherCoursesViewModel teacherCoursesViewModel;

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);

        teacherCoursesViewModel = new ViewModelProvider(this).get(TeacherCoursesViewModel.class);
        teacherCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setCourses(queryDocumentSnapshots));
    }
}
