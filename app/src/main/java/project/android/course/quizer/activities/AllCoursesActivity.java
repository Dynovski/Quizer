package project.android.course.quizer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QuerySnapshot;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.StudentCourseAdapter;
import project.android.course.quizer.viewmodels.AllCoursesViewModel;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;

public class AllCoursesActivity extends AppCompatActivity
{
    private AllCoursesViewModel coursesViewModel;
    private SubscribedCoursesViewModel subscribedCoursesViewModel;
    private StudentCourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentCourseAdapter(this);
        recyclerView.setAdapter(adapter);

        coursesViewModel = new ViewModelProvider(this).get(AllCoursesViewModel.class);
        coursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setAllCourses(queryDocumentSnapshots));
        subscribedCoursesViewModel = new ViewModelProvider(this).get(SubscribedCoursesViewModel.class);
        subscribedCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setSubscribedCourses(queryDocumentSnapshots));
    }
}
