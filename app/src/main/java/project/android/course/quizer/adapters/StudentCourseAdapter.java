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
import project.android.course.quizer.firebaseObjects.Course;

// Course adapter creates views for course list items and replaces the content of some of the views
// with new data items when the original item is no longer visible
public class StudentCourseAdapter extends RecyclerView.Adapter<StudentCourseAdapter.StudentCourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> courses;
    private Context applicationContext;

    class StudentCourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView name;
        private final TextView enrolledInfo;

        public StudentCourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_course_name);
            enrolledInfo = itemView.findViewById(R.id.text_view_enrolled);
        }

    }

    public StudentCourseAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        courses = new ArrayList<Course>();
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public StudentCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.course_item_student, parent, false);
        return new StudentCourseViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull StudentCourseViewHolder holder, int position)
    {
        if(courses != null)
        {
            Course current = courses.get(position);
            holder.name.setText(current.getCourseName());
            holder.enrolledInfo.setText(R.string.course_subscribe);
            holder.enrolledInfo.setOnClickListener(v -> {
                holder.enrolledInfo.setText(holder.enrolledInfo.getText().toString().equals("Subscribe") ?
                        R.string.course_unsubscribe : R.string.course_subscribe);
                // MARK SUBSCRIPTION IN DATABASE
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull StudentCourseViewHolder holder)
    {
        holder.enrolledInfo.setOnClickListener(null);
        super.onViewRecycled(holder);
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
