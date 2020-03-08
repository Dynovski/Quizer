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

// Adapter for displaying test tests to do by the user, it contains the list of tests to do
// and displays them, it also sets OnClickListener to begin solving given test
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
            nameTextView = itemView.findViewById(R.id.test_name_text_view);
            beginTestTextView = itemView.findViewById(R.id.start_test_text_view);
        }

    }

    public ToDoTestsAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        testsToDo = new ArrayList<>();
    }

    @NonNull
    @Override
    public ToDoTestsAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.test_to_do_item, parent, false);
        return new ToDoTestsAdapter.TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoTestsAdapter.TestViewHolder holder, int position)
    {
        if(testsToDo != null)
        {
            Test current = testsToDo.get(position);
            holder.nameTextView.setText(current.getTestName());

            holder.beginTestTextView.setOnClickListener(v -> {
                Intent solveTestIntent = new Intent(applicationContext, SolveTestActivity.class);
                solveTestIntent.putExtra("testName", current.getTestName());
                solveTestIntent.putExtra("numOfQuestions", current.getNumOfQuestions());
                solveTestIntent.putExtra("deadline", current.getDueDate());
                solveTestIntent.putExtra("courseName", current.getCourseName());
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

    @Override
    public int getItemCount()
    {
        if(testsToDo != null)
            return testsToDo.size();
        else
            return 0;
    }
}
