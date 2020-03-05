package project.android.course.quizer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.SolveTestActivity;
import project.android.course.quizer.firebaseObjects.Test;

public class ToDoTestsAdapter extends RecyclerView.Adapter<ToDoTestsAdapter.TestViewHolder>
{
    private final LayoutInflater inflater;
    private List<Test> testsToDo;
    private Context applicationContext;

    class TestViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameTextView;
        private final TextView beginTestTextView;

        public TestViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_test_name);
            beginTestTextView = itemView.findViewById(R.id.text_view_start_test);
        }

    }

    public ToDoTestsAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        testsToDo = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ToDoTestsAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.test_to_do_item, parent, false);
        return new ToDoTestsAdapter.TestViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ToDoTestsAdapter.TestViewHolder holder, int position)
    {
        if(testsToDo != null)
        {
            Test current = testsToDo.get(position);
            holder.nameTextView.setText(current.getTestName());

            holder.beginTestTextView.setOnClickListener(v -> {
                Intent solveTestIntent = new Intent(applicationContext, SolveTestActivity.class);
                solveTestIntent.putExtra("TESTNAME", current.getTestName());
                solveTestIntent.putExtra("NUMOFQUESTIONS", current.getNumOfQuestions());
                solveTestIntent.putExtra("DEADLINE", current.getDueDate());
                solveTestIntent.putExtra("COURSENAME", current.getCourseName());
               applicationContext.startActivity(solveTestIntent);
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull ToDoTestsAdapter.TestViewHolder holder)
    {
        holder.beginTestTextView.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    public void setTestsToDo(List<DocumentSnapshot> documents)
    {
        this.testsToDo.clear();
        for(DocumentSnapshot document : documents)
            this.testsToDo.add(document.toObject(Test.class));
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        if(testsToDo != null)
            return testsToDo.size();
        else
            return 0;
    }
}
