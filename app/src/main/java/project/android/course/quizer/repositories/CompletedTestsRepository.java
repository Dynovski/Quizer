package project.android.course.quizer.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;

public class CompletedTestsRepository
{
    private static final CollectionReference SUBSCRIBED_COURSES_REFERENCE =
            FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("SubscribedCourses");
    private FirebaseQueryLiveData completedTests;

    public CompletedTestsRepository(Course course)
    {
        completedTests = new FirebaseQueryLiveData(SUBSCRIBED_COURSES_REFERENCE
                .document(course.getCourseName()).collection("CompletedTests"));
    }

    public FirebaseQueryLiveData getCourses()
    {
        return completedTests;
    }
}
