package project.android.course.quizer.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;

public class SubscribedCoursesRepository
{
    private static final CollectionReference USERS_SUBSCRIBED_COURSES_REFERENCE =
            FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("SubscribedCourses");
    private FirebaseQueryLiveData courses;

    public SubscribedCoursesRepository()
    {
        courses = new FirebaseQueryLiveData(USERS_SUBSCRIBED_COURSES_REFERENCE);
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
