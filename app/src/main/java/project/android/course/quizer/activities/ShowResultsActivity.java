package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.adapters.ResultsAdapter;
import project.android.course.quizer.viewmodels.CompletedTestViewModel;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;

public class ShowResultsActivity extends AppCompatActivity
{
    private ResultsAdapter adapter;
    private CompletedTestViewModel completedTestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultsAdapter(this);
        recyclerView.setAdapter(adapter);

        completedTestViewModel = new ViewModelProvider(this).get(CompletedTestViewModel.class);
        completedTestViewModel.getCompletedTests().observe(this, queryDocumentSnapshots -> adapter.setCourses(queryDocumentSnapshots));
    }
}
