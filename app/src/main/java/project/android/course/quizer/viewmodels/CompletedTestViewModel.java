package project.android.course.quizer.viewmodels;

import androidx.lifecycle.ViewModel;

import project.android.course.quizer.firebaseObjects.FirebaseQueryLiveData;
import project.android.course.quizer.repositories.CompletedTestsRepository;

public class CompletedTestViewModel extends ViewModel
{
    private FirebaseQueryLiveData completedTests;

    public CompletedTestViewModel()
    {
        CompletedTestsRepository repository = new CompletedTestsRepository();
        completedTests = repository.getCompletedTests();
    }

    public FirebaseQueryLiveData getCompletedTests()
    {
        return completedTests;
    }
}
