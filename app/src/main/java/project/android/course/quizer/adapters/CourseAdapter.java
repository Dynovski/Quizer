package project.android.course.quizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.fragments.CourseDetailsDialogFragment;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> courses;
    private Context context;

    class CourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView name;

        public CourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_course_name);
        }
    }

    public CourseAdapter(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        courses = new ArrayList<Course>();
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.course_item_teacher, parent, false);
        return new CourseViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position)
    {
        if(courses != null)
        {
            Course current = courses.get(position);
            holder.name.setText(current.getCourseName());

            holder.name.setOnClickListener(v -> {
                DialogFragment newFragment = new CourseDetailsDialogFragment(current);
                newFragment.show(((FragmentActivity)context).getSupportFragmentManager(), current.getCourseName() + "DetailDialog");
            });
        }
    }

    public void setCourses(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.courses.clear();
        for(DocumentSnapshot document : documents)
            this.courses.add(document.toObject(Course.class));
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        if(courses != null)
            return courses.size();
        else
            return 0;
    }
}