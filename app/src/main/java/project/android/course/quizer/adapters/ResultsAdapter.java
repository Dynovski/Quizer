package project.android.course.quizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.CompletedTest;

// Adapter for displaying test results, it contains the list of completed tests and displays them
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>
{
    private final LayoutInflater inflater;
    private List<CompletedTest> results;
    private Context context;

    class ResultViewHolder extends RecyclerView.ViewHolder
    {
        private TextView courseNameTextView;
        private TextView testNameTextView;
        private TextView testScoreTextView;

        public ResultViewHolder(@NonNull View itemView)
        {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.result_course_name_text_view);
            testNameTextView = itemView.findViewById(R.id.result_test_name_text_view);
            testScoreTextView = itemView.findViewById(R.id.result_score_text_view);
        }
    }

    public ResultsAdapter(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        results = new ArrayList<>();
    }

    @NonNull
    @Override
    public ResultsAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.result_list_item, parent, false);
        return new ResultsAdapter.ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ResultViewHolder holder, int position)
    {
        if(results != null)
        {
            CompletedTest current = results.get(position);
            holder.courseNameTextView.setText(current.getCourseName());
            holder.testNameTextView.setText(current.getTestName());
            holder.testScoreTextView.setText(String.format("%d/%d",
                    current.getScore(), current.getNumberOfQuestions()));
        }
    }

    public void setResults(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.results.clear();
        for(DocumentSnapshot document : documents)
            this.results.add(document.toObject(CompletedTest.class));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        if(results != null)
            return results.size();
        else
            return 0;
    }
}