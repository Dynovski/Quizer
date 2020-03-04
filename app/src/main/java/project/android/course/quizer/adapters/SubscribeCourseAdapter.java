package project.android.course.quizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.singletons.CurrentUser;

// Course adapter creates views for course list items and replaces the content of some of the views
// with new data items when the original item is no longer visible
public class SubscribeCourseAdapter extends RecyclerView.Adapter<SubscribeCourseAdapter.SubscribeCourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> allCourses;
    private List<String> subscribedCourses;
    private Context applicationContext;

    class SubscribeCourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView name;
        private final TextView enrolledInfo;

        public SubscribeCourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_course_name);
            enrolledInfo = itemView.findViewById(R.id.text_view_enrolled);
        }

    }

    public SubscribeCourseAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        allCourses = new ArrayList<>();
        subscribedCourses = new ArrayList<>();
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public SubscribeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.course_item_student, parent, false);
        return new SubscribeCourseViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull SubscribeCourseViewHolder holder, int position)
    {
        if(allCourses != null)
        {
            Course current = allCourses.get(position);
            holder.name.setText(current.getCourseName());
            if(subscribedCourses.contains(current.getCourseName()))
                holder.enrolledInfo.setText(R.string.course_unsubscribe);
            else
                holder.enrolledInfo.setText(R.string.course_subscribe);
            //TODO: UPRZATNAC
            holder.enrolledInfo.setOnClickListener(v -> {
                if(holder.enrolledInfo.getText().toString().equals(applicationContext.getResources().getString(R.string.course_subscribe)))
                {
                    holder.enrolledInfo.setText(R.string.course_unsubscribe);

                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses").document(current.getCourseName()).set(current);

                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(current.getCourseName()).collection("EnrolledStudents")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(CurrentUser.getCurrentUser());
                }
                else
                {
                    holder.enrolledInfo.setText(R.string.course_subscribe);
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses").document(current.getCourseName())
                            .delete();

                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(current.getCourseName()).collection("EnrolledStudents")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .delete();
                }
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull SubscribeCourseViewHolder holder)
    {
        holder.enrolledInfo.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    public void setAllCourses(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.allCourses.clear();
        for(DocumentSnapshot document : documents)
            this.allCourses.add(document.toObject(Course.class));
        notifyDataSetChanged();
    }

    public void setSubscribedCourses(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.subscribedCourses.clear();
        for(DocumentSnapshot document : documents)
            this.subscribedCourses.add((String) document.get("courseName"));
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        if(allCourses != null)
            return allCourses.size();
        else
            return 0;
    }
}
