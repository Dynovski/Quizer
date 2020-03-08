package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.ToDoTestsAdapter;
import project.android.course.quizer.singletons.CurrentUser;
import project.android.course.quizer.viewmodels.CompletedTestViewModel;
import project.android.course.quizer.viewmodels.SubscribedCoursesViewModel;

// Activity coordinating the display of tests to be finished by the user, it checks the deadline for
// the test and if the test was already done by the user, then it displays tests that are valid
// and user can begin the test
public class TestsToDoActivity extends AppCompatActivity
{
    private static final String TAG = "TESTS_TO_DO_DEBUG";

    private ToDoTestsAdapter adapter;
    private CompletedTestViewModel completedTestViewModel;
    private SubscribedCoursesViewModel subscribedCoursesViewModel;

    private FirebaseFirestore database;

    private List<String> subscribedCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        database = FirebaseFirestore.getInstance();

        subscribedCoursesViewModel = new ViewModelProvider(this).get(SubscribedCoursesViewModel.class);
        subscribedCoursesViewModel.getCourses().observe(this, this::setSubscribedCourses);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoTestsAdapter(this);
        recyclerView.setAdapter(adapter);

        // Checking if user has any completed tests (if not completed tests listener won't work)
        // If not then adapter must be populated with all the valid and subscribed tests
        database.collection("Users").document(CurrentUser.getCurrentUser().getUserId())
                .collection("CompletedTests").limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.getDocuments().isEmpty())
            {
                database.collection("Tests").whereGreaterThan("dueDate",
                        new Timestamp(new Date())).get()
                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                            List<DocumentSnapshot> validTests = queryDocumentSnapshots1.getDocuments();
                            ArrayList<DocumentSnapshot> testsToDo = new ArrayList<>();
                            for(DocumentSnapshot test : validTests)
                            {
                                String courseName = test.get("courseName").toString();
                                if(subscribedCourses.contains(courseName))
                                    testsToDo.add(test);
                            }
                            adapter.setTestsToDo(testsToDo);
                        })
                        .addOnFailureListener(e -> Log.d(TAG, "Couldn't fetch valid tests\n" + e.toString()));
            }
        });

        // Each time completed tests changes populate adapter with valid tests
        completedTestViewModel = new ViewModelProvider(this).get(CompletedTestViewModel.class);
        completedTestViewModel.getCompletedTests().observe(this, queryDocumentSnapshots -> {
            database.collection("Tests").whereGreaterThan("dueDate",
                    new Timestamp(new Date())).get()
                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                        List<DocumentSnapshot> validTests = queryDocumentSnapshots1.getDocuments();
                        List<DocumentSnapshot> completedTests = queryDocumentSnapshots.getDocuments();
                        ArrayList<DocumentSnapshot> testsToDo = new ArrayList<>();
                        outer:
                        for(DocumentSnapshot test : validTests)
                        {
                            String testName = test.get("testName").toString();
                            String courseName = test.get("courseName").toString();
                            if(!subscribedCourses.contains(courseName))
                                continue;
                            for(DocumentSnapshot completedTest : completedTests)
                            {
                                if(testName.equals(completedTest.get("testName")))
                                    continue outer;
                            }
                            testsToDo.add(test);
                        }
                        adapter.setTestsToDo(testsToDo);
                    })
                    .addOnFailureListener(e -> Log.d(TAG, "Couldn't fetch valid tests\n" + e.toString()));
        });
    }

    private void setSubscribedCourses(QuerySnapshot snapshot)
    {
        List<DocumentSnapshot> documents = snapshot.getDocuments();
        this.subscribedCourses.clear();
        for(DocumentSnapshot document : documents)
            this.subscribedCourses.add((String) document.get("courseName"));
    }
}
