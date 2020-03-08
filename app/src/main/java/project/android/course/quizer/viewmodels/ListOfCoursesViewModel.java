package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.ListOfCoursesRepository;

// ViewModel containing liveData to all courses
public class ListOfCoursesViewModel extends ViewModel
{
    private FirebaseQueryLiveData courses;

    public ListOfCoursesViewModel()
    {
        ListOfCoursesRepository repository = new ListOfCoursesRepository();
        courses = repository.getCourses();
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
