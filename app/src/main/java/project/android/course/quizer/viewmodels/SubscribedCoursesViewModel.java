package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.SubscribedCoursesRepository;

public class SubscribedCoursesViewModel extends ViewModel
{
    private FirebaseQueryLiveData courses;

    public SubscribedCoursesViewModel()
    {
        SubscribedCoursesRepository repository = new SubscribedCoursesRepository();
        courses = repository.getCourses();
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
