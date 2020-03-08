package project.android.course.quizer.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.ResultsAdapter;
import project.android.course.quizer.viewmodels.CompletedTestViewModel;

// Activity coordinating the display of the list of test results for given user, recyclerView
// operates on LiveData, so it is updated automatically when changes in database occur
public class ShowResultsActivity extends AppCompatActivity
{
    private ResultsAdapter adapter;
    private CompletedTestViewModel completedTestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultsAdapter(this);
        recyclerView.setAdapter(adapter);

        completedTestViewModel = new ViewModelProvider(this).get(CompletedTestViewModel.class);
        completedTestViewModel.getCompletedTests().observe(this, queryDocumentSnapshots ->
                adapter.setResults(queryDocumentSnapshots));
    }
}
