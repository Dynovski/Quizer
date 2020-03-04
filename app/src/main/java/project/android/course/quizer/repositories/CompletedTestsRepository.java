package project.android.course.quizer.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;

public class CompletedTestsRepository
{
    private static final CollectionReference COMPLETED_TESTS_REFERENCE =
            FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("CompletedTests");
    private FirebaseQueryLiveData completedTests;

    public CompletedTestsRepository()
    {
        completedTests = new FirebaseQueryLiveData(COMPLETED_TESTS_REFERENCE);
    }

    public FirebaseQueryLiveData getCompletedTests()
    {
        return completedTests;
    }
}
