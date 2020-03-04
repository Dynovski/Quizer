package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.ListOfCoursesRepository;

public class AllCoursesViewModel extends ViewModel
{
    private FirebaseQueryLiveData courses;

    public AllCoursesViewModel()
    {
        ListOfCoursesRepository repository = new ListOfCoursesRepository();
        courses = repository.getCourses();
    }

    public FirebaseQueryLiveData getCourses()
    {
        return courses;
    }
}
