package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.TeacherCoursesRepository;
import project.android.course.quizer.singletons.CurrentUser;

// ViewModel containing liveData to courses created by given teacher
public class TeacherCoursesViewModel extends ViewModel
{
    private FirebaseQueryLiveData courses;

    public TeacherCoursesViewModel()
    {
        TeacherCoursesRepository repository = new TeacherCoursesRepository(CurrentUser.getCurrentUser().getName());
        courses = repository.getCourses();
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
