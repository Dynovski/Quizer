package project.android.course.quizer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.adapters.ToDoTestsAdapter;
import project.android.course.quizer.repositories.CompletedTestsRepository;
import project.android.course.quizer.viewmodels.CompletedTestViewModel;
import project.android.course.quizer.viewmodels.TeacherCoursesViewModel;

public class TestsToDoActivity extends AppCompatActivity
{
    private ToDoTestsAdapter adapter;
    private CompletedTestViewModel completedTestViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoTestsAdapter(this);
        recyclerView.setAdapter(adapter);


        completedTestViewModel = new ViewModelProvider(this).get(CompletedTestViewModel.class);
        //MUSI ISTNIEC WPIS W COMPLETED TESTS BO BEZ TEGO NIE DZIALA
        completedTestViewModel.getCompletedTests().observe(this, queryDocumentSnapshots -> {
                    FirebaseFirestore.getInstance().collection("Tests").whereGreaterThan("dueDate",
                            new Timestamp(new Date())).get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                List<DocumentSnapshot> validTests = queryDocumentSnapshots1.getDocuments();
                                List<DocumentSnapshot> completedTests = queryDocumentSnapshots.getDocuments();
                                ArrayList<DocumentSnapshot> testsToDo = new ArrayList<>();
                                outer:
                                for(DocumentSnapshot test : validTests)
                                {
                                    String testName = test.get("testName").toString();
                                    for(DocumentSnapshot completedTest : completedTests)
                                    {
                                        if(testName.equals(completedTest.get("testName")))
                                            continue outer;
                                    }
                                    testsToDo.add(test);
                                }
                                adapter.setTestsToDo(testsToDo);
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                            });
                });
    }
}
