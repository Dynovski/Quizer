package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.SubscribedCoursesRepository;

// ViewModel containing liveData to currently logged in user's subscribed courses
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
