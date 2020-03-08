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
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.singletons.CurrentUser;

// Adapter for displaying list of all courses with possibility to subscribe to them, it contains
// the list of subscribed courses and displays them, when subscribing to the courses, course that
// user subscribed to is saved in database in SubscribedCourses collection, when unsubscribing it is deleted
public class SubscribeCourseAdapter extends RecyclerView.Adapter<SubscribeCourseAdapter.SubscribeCourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> allCourses;
    private List<String> subscribedCourses;
    private Context applicationContext;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    class SubscribeCourseViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameTextView;
        private final TextView enrolledInfoTextView;

        public SubscribeCourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.course_name_text_view);
            enrolledInfoTextView = itemView.findViewById(R.id.enrolled_text_view);
        }

    }

    public SubscribeCourseAdapter(Context context)
    {
        applicationContext = context;
        inflater = LayoutInflater.from(context);
        allCourses = new ArrayList<>();
        subscribedCourses = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubscribeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = inflater.inflate(R.layout.subscribeable_course_item, parent, false);
        return new SubscribeCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribeCourseViewHolder holder, int position)
    {
        if(allCourses != null)
        {
            Course current = allCourses.get(position);
            holder.nameTextView.setText(current.getCourseName());

            if(subscribedCourses.contains(current.getCourseName()))
                holder.enrolledInfoTextView.setText(R.string.unsubscribe);
            else
                holder.enrolledInfoTextView.setText(R.string.subscribe);

            holder.enrolledInfoTextView.setOnClickListener(v -> {
                if(holder.enrolledInfoTextView.getText().toString().equals(applicationContext.getResources().getString(R.string.subscribe)))
                {
                    holder.enrolledInfoTextView.setText(R.string.unsubscribe);

                    database.collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses")
                            .document(current.getCourseName()).set(current);

                    database.collection("Courses").document(current.getCourseName())
                            .collection("EnrolledStudents")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(CurrentUser.getCurrentUser());
                } else
                {
                    holder.enrolledInfoTextView.setText(R.string.subscribe);
                    database.collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses")
                            .document(current.getCourseName()).delete();

                    database.collection("Courses").document(current.getCourseName())
                            .collection("EnrolledStudents")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .delete();
                }
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull SubscribeCourseViewHolder holder)
    {
        holder.enrolledInfoTextView.setOnClickListener(null);
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

    @Override
    public int getItemCount()
    {
        if(allCourses != null)
            return allCourses.size();
        else
            return 0;
    }
}
