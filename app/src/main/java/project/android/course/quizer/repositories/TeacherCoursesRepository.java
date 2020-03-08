package project.android.course.quizer.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;

// Repository containing liveData to courses created by given teacher
public class TeacherCoursesRepository
{
    private static final CollectionReference COURSES_REFERENCE =
            FirebaseFirestore.getInstance().collection("Courses");
    private FirebaseQueryLiveData courses;

    public TeacherCoursesRepository(String teacherName)
    {
        this.courses = new FirebaseQueryLiveData(COURSES_REFERENCE.whereEqualTo("teacher", teacherName));
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
