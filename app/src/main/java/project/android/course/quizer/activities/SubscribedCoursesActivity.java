package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;
import project.android.course.quizer.viewmodels.TeacherCoursesViewModel;

public class SubscribedCoursesActivity extends AppCompatActivity
{
    private CourseAdapter adapter;
    private SubscribedCoursesViewModel subscribedCoursesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);

        subscribedCoursesViewModel = new ViewModelProvider(this).get(SubscribedCoursesViewModel.class);
        subscribedCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setCourses(queryDocumentSnapshots));
    }
}
