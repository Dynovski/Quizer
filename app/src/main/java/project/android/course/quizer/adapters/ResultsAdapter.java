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

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>
{
    private final LayoutInflater inflater;
    private List<CompletedTest> results;
    private Context context;

    class ResultViewHolder extends RecyclerView.ViewHolder
    {
        private TextView courseName;
        private TextView testName;
        private TextView testScore;

        public ResultViewHolder(@NonNull View itemView)
        {
            super(itemView);
            courseName = itemView.findViewById(R.id.result_course_name_tv);
            testName = itemView.findViewById(R.id.result_test_name_tv);
            testScore = itemView.findViewById(R.id.result_score_tv);
        }
    }

    public ResultsAdapter(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        results = new ArrayList<>();
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ResultsAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.result_list_item, parent, false);
        return new ResultsAdapter.ResultViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ResultViewHolder holder, int position)
    {
        if(results != null)
        {
            CompletedTest current = results.get(position);
            holder.courseName.setText(current.getCourseName());
            holder.testName.setText(current.getTestName());
            holder.testScore.setText(String.format("%d/%d", current.getScore(), current.getNumberOfQuestions()));
        }
    }

    public void setCourses(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.results.clear();
        for(DocumentSnapshot document : documents)
            this.results.add(document.toObject(CompletedTest.class));
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        if(results != null)
            return results.size();
        else
            return 0;
    }
}