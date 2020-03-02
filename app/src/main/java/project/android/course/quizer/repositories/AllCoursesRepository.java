package project.android.course.quizer.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;

public class AllCoursesRepository
{
    private static final CollectionReference COURSES_REFERENCE =
            FirebaseFirestore.getInstance().collection("Courses");
    private FirebaseQueryLiveData courses;

    public AllCoursesRepository()
    {
        courses = new FirebaseQueryLiveData(COURSES_REFERENCE);
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
