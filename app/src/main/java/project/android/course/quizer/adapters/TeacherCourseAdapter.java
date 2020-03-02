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

public class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.TeacherCourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> courses;
    private Context applicationContext;

    class TeacherCourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView name;

        public TeacherCourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_course_name);
        }
    }

    public TeacherCourseAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        courses = new ArrayList<Course>();
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TeacherCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.course_item_teacher, parent, false);
        return new TeacherCourseViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TeacherCourseViewHolder holder, int position)
    {
        if(courses != null)
        {
            Course current = courses.get(position);
            holder.name.setText(current.getCourseName());
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