package project.android.course.quizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;

// Course adapter creates views for course list items and replaces the content of some of the views
// with new data items when the original item is no longer visible
public class StudentCourseAdapter extends RecyclerView.Adapter<StudentCourseAdapter.StudentCourseViewHolder>
{
    private final LayoutInflater inflater;
    private List<Course> allCourses;
    private List<String> subscribedCourses;
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
        allCourses = new ArrayList<>();
        subscribedCourses = new ArrayList<>();
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
                    Map<String, Object> newSubscribedCourse = new HashMap<>();
                    newSubscribedCourse.put("courseName", current.getCourseName());
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses").document().set(newSubscribedCourse);
                    FirebaseFirestore.getInstance().collection("Courses")
                            .whereEqualTo("courseName", current.getCourseName())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> queryDocumentSnapshots.getDocuments().get(0).getReference()
                                    .collection("EnrolledStudents").document()
                                    .set(CurrentUser.getCurrentUser()))
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                            });
                }
                else
                {
                    holder.enrolledInfo.setText(R.string.course_subscribe);
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("SubscribedCourses").whereEqualTo("courseName", current.getCourseName())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
                            })
                            .addOnFailureListener( e -> e.printStackTrace());
                    FirebaseFirestore.getInstance().collection("Courses")
                            .whereEqualTo("courseName", current.getCourseName())
                            .get().addOnSuccessListener(queryDocumentSnapshots -> queryDocumentSnapshots.getDocuments().get(0).getReference()
                                .collection("EnrolledStudents").whereEqualTo("name", CurrentUser.getCurrentUser().getName())
                                .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    queryDocumentSnapshots1.getDocuments().get(0).getReference().delete();
                                })
                                    .addOnFailureListener(e -> e.printStackTrace()))
                            .addOnFailureListener(e -> e.printStackTrace());
                }

            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull StudentCourseViewHolder holder)
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
