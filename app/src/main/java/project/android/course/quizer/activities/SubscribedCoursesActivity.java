package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;

// Activity coordinating the display of the list of courses subscribed by user, recyclerView
// operates on LiveData, so it is updated automatically when changes in database occur
public class SubscribedCoursesActivity extends AppCompatActivity
{
    private CourseAdapter adapter;
    private SubscribedCoursesViewModel subscribedCoursesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);

        subscribedCoursesViewModel = new ViewModelProvider(this).get(SubscribedCoursesViewModel.class);
        subscribedCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots ->
                adapter.setCourses(queryDocumentSnapshots));
    }
}
