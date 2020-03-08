package project.android.course.quizer.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import project.android.course.quizer.R;
import project.android.course.quizer.adapters.CourseAdapter;
import project.android.course.quizer.firebaseObjects.Course;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;
import project.android.course.quizer.viewmodels.TeacherCoursesViewModel;

// Activity coordinating the display of the list of courses of a given teacher, its recyclerView's cells
// recyclerView operates on LiveData, so it is updated automatically when changes in database occur
public class TeacherCoursesActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    private static final String TAG = "TEACHER_COURSES_DEBUG";
    private static final int EDIT_COURSE_REQUEST_CODE = 1;
    private TeacherCoursesViewModel teacherCoursesViewModel;

    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_courses_acitvity);

        RecyclerView recyclerView = findViewById(R.id.coursesRecycleView);
        recyclerView.setHasFixedSize(true);
        adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teacherCoursesViewModel = new ViewModelProvider(this).get(TeacherCoursesViewModel.class);
        teacherCoursesViewModel.getCourses().observe(this, queryDocumentSnapshots -> adapter.setCourses(queryDocumentSnapshots));
    }

    public void showPopupMenu(View v)
    {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.contextual_menu_teacher);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.edit_course_item:
                Course selectedCourse = adapter.getSelectedCourse();
                Intent editCourseIntent = new Intent(this, EditCourseActivity.class);
                editCourseIntent.putExtra("courseName", selectedCourse.getCourseName());
                editCourseIntent.putExtra("courseDescription", selectedCourse.getDescription());
                startActivityForResult(editCourseIntent, EDIT_COURSE_REQUEST_CODE);
                return true;
            case R.id.delete_course_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to delete this course?");
                builder.setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(adapter.getSelectedCourse().getCourseName()).collection("EnrolledStudents").get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<DocumentSnapshot> enrolledStudents = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot student : enrolledStudents)
                                {
                                    String uid = student.toObject(User.class).getUserId();
                                    FirebaseFirestore.getInstance().collection("Users").document(uid)
                                            .collection("SubscribedCourses")
                                            .document(adapter.getSelectedCourse().getCourseName()).delete()
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully deleted course from subscribed courses"))
                                            .addOnFailureListener(e -> Log.d(TAG, "Couldn't delete course from subscribed courses\n" + e.toString()));
                                }
                                FirebaseFirestore.getInstance().collection("Courses")
                                        .document(adapter.getSelectedCourse().getCourseName()).delete()
                                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully deleted course from courses"))
                                        .addOnFailureListener(e -> Log.d(TAG, "Couldn't delete course from courses\n" + e.toString()));
                            });
                });
                builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.add_test_item:
                Intent testIntent = new Intent(this, CreateTestActivity.class);
                testIntent.putExtra("COURSENAME", adapter.getSelectedCourse().getCourseName());
                startActivity(testIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_COURSE_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                String oldCourseName = data.getStringExtra("oldCourseName");
                String newCourseName = data.getStringExtra("newCourseName");
                String courseDescription = data.getStringExtra("courseDescription");


                if(namesAreDifferent(oldCourseName, newCourseName))
                {
                    // Getting all users enrolled for the course
                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(oldCourseName).collection("EnrolledStudents").get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<DocumentSnapshot> enrolledStudents = queryDocumentSnapshots.getDocuments();
                                List<User> studentsEnrolledToCourse = new ArrayList<>();

                                Course editedCourse = new Course(newCourseName,
                                        CurrentUser.getCurrentUser().getName(), courseDescription);

                                for(DocumentSnapshot enrolledStudent : enrolledStudents)
                                {
                                    User user = enrolledStudent.toObject(User.class);
                                    studentsEnrolledToCourse.add(user);
                                    deleteCourseFromSubscribedCourses(user.getUserId(), oldCourseName);
                                    setSubscribedCourse(user.getUserId(), newCourseName, editedCourse);
                                    updateCourseNameInCompletedTests(user.getUserId(), oldCourseName, newCourseName);
                                    // deleting document from enrolled students collection
                                    enrolledStudent.getReference().delete();
                                }

                                deleteCourse(oldCourseName);
                                setCourse(newCourseName, editedCourse, studentsEnrolledToCourse);
                                updateCourseNameInTests(oldCourseName, newCourseName);

                                Log.d(TAG, "Retrieved all users from course");
                            })
                            .addOnFailureListener(e -> Log.d(TAG, "Couldn't retrieve all users from course\n" + e.toString()));
                }
                else
                {
                    Course editedCourse = new Course(newCourseName,
                            CurrentUser.getCurrentUser().getName(), courseDescription);

                    setCourse(newCourseName, editedCourse, null);

                    FirebaseFirestore.getInstance().collection("Courses")
                            .document(oldCourseName).collection("EnrolledStudents").get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<DocumentSnapshot> enrolledStudents = queryDocumentSnapshots.getDocuments();

                                for(DocumentSnapshot enrolledStudent : enrolledStudents)
                                {
                                    User user = enrolledStudent.toObject(User.class);
                                    setSubscribedCourse(user.getUserId(), newCourseName, editedCourse);
                                }
                            })
                            .addOnFailureListener(e -> Log.d(TAG, "Couldn't retrieve all users from course\n" + e.toString()));

                }
                Toast.makeText(this, "Successfully edited course", Toast.LENGTH_SHORT).show();
            } else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Cancelled editing course", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Cannot edit course", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean namesAreDifferent(String name1, String name2)
    {
        return !name1.equals(name2);
    }

    private void deleteCourseFromSubscribedCourses(String userId, String courseName)
    {
        FirebaseFirestore.getInstance().collection("Users").document(userId)
                .collection("SubscribedCourses")
                .document(courseName).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully deleted course content from subscribed courses"))
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't delete course content from subscribed courses\n" + e.toString()));
    }

    private void updateCourseNameInCompletedTests(String userId, String oldCourseName, String newCourseName)
    {
        FirebaseFirestore.getInstance().collection("Users").document(userId)
                .collection("CompletedTests").whereEqualTo("courseName", oldCourseName).get()
                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                    List<DocumentSnapshot> completedTests = queryDocumentSnapshots1.getDocuments();
                    for(DocumentSnapshot completedTest : completedTests)
                        completedTest.getReference().update("courseName", newCourseName)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully updated course in completed tests"))
                                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course in completed tests\n" + e.toString()));
                })
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course in completed tests\n" + e.toString()));
    }

    private void deleteCourse(String courseName)
    {
        FirebaseFirestore.getInstance().collection("Courses")
                .document(courseName).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully deleted course content from courses"))
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't delete course content from courses\n" + e.toString()));
    }

    private void updateCourseNameInTests(String oldCourseName, String newCourseName)
    {
        FirebaseFirestore.getInstance().collection("Tests").whereEqualTo("courseName", oldCourseName).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> tests = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot test : tests)
                        test.getReference().update("courseName", newCourseName)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully updated course in tests"))
                                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course in tests\n" + e.toString()));
                })
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course in completed tests\n" + e.toString()));
    }

    private void setCourse(String courseName, Course course, List<User> enrolledStudents)
    {
        FirebaseFirestore.getInstance().collection("Courses")
                .document(courseName).set(course)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully updated course content from courses");
                    // If name changed therefore document rewrite enrolled students
                    if(enrolledStudents != null)
                    {
                        for(User student : enrolledStudents)
                            FirebaseFirestore.getInstance().collection("Courses")
                                    .document(courseName).collection("EnrolledStudents").document().set(student);
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course content from courses\n" + e.toString()));
    }

    private void setSubscribedCourse(String userId, String courseName, Course course)
    {
        FirebaseFirestore.getInstance().collection("Users").document(userId)
                .collection("SubscribedCourses")
                .document(courseName).set(course)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully updated course content from subscribed courses"))
                .addOnFailureListener(e -> Log.d(TAG, "Couldn't update course content from subscribed courses\n" + e.toString()));
    }
}
