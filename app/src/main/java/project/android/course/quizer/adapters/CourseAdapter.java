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
import project.android.course.quizer.activities.TeacherCoursesActivity;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.fragments.CourseDetailsDialogFragment;

// Adapter for displaying courses using one TextView, it contains the list of courses,
// displays them and sets onClickListeners, it checks what user type is using it and allows teachers
// to perform more actions on items than students, students can only see detailed information about
// courses, teachers can edit, delete them or create tests for given course
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> courses;
    private Context context;
    private Course selectedCourse;

    class CourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameTextView;

        public CourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.course_name_text_view);
        }
    }

    public CourseAdapter(Context context)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        courses = new ArrayList<>();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.simple_course_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position)
    {
        if(courses != null)
        {
            Course current = courses.get(position);
            holder.nameTextView.setText(current.getCourseName());

            holder.itemView.setOnClickListener(v -> {
                DialogFragment newFragment = new CourseDetailsDialogFragment(current);
                newFragment.show(((FragmentActivity) context).getSupportFragmentManager(),
                        current.getCourseName() + "DetailDialog");
            });

            if(context instanceof TeacherCoursesActivity)
            {
                holder.itemView.setOnLongClickListener(v -> {
                    selectedCourse = courses.get(position);
                    ((TeacherCoursesActivity) context).showPopupMenu(v);
                    return true;
                });
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull CourseViewHolder holder)
    {
        holder.itemView.setOnLongClickListener(null);
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

    public Course getSelectedCourse()
    {
        return selectedCourse;
    }

    @Override
    public int getItemCount()
    {
        if(courses != null)
            return courses.size();
        else
            return 0;
    }
}