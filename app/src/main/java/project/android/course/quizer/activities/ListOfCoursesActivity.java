package project.android.course.quizer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.SubscribeCourseAdapter;
import project.android.course.quizer.viewmodels.ListOfCoursesViewModel;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;

// Activity coordinating the display of the list of all available courses, its recyclerView's cells
// adapt to each user's already subscribed courses to display accurate data, recyclerView additionally
// operates on LiveData, so it is updated automatically when changes in database occur
public class ListOfCoursesActivity extends AppCompatActivity
{
    private ListOfCoursesViewModel coursesViewModel;
    private SubscribedCoursesViewModel subscribedCoursesViewModel;
    private SubscribeCourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubscribeCourseAdapter(this);
        recyclerView.setAdapter(adapter);

        coursesViewModel = new ViewModelProvider(this).get(ListOfCoursesViewModel.class);
        coursesViewModel.getCourses().observe(this, queryDocumentSnapshots ->
                adapter.setAllCourses(queryDocumentSnapshots));

        subscribedCoursesViewModel = new ViewModelProvider(this).get(SubscribedCoursesViewModel.class);
        subscribedCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots ->
                adapter.setSubscribedCourses(queryDocumentSnapshots));
    }
}
