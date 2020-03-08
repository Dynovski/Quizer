package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import project.android.course.quizer.R;
import project.android.course.quizer.fragments.AddCourseDialogFragment;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating actions available in teacher's home screen, upon choosing one of the
// options it delegates user to new activities
public class TeacherHomeScreenActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_screen);

        findViewById(R.id.sign_out_card).setOnClickListener(this);
        findViewById(R.id.my_account_card).setOnClickListener(this);
        findViewById(R.id.create_course_card).setOnClickListener(this);
        findViewById(R.id.teacher_courses_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.teacher_courses_card:
                startActivity(new Intent(this, TeacherCoursesActivity.class));
                break;
            case R.id.my_account_card:
                startActivity(new Intent(this, UserAccountActivity.class));
                break;
            case R.id.sign_out_card:
                CurrentUser.logOutUser();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.create_course_card:
                DialogFragment newFragment = new AddCourseDialogFragment();
                newFragment.show(getSupportFragmentManager(), "ADD_COURSE_FRAGMENT_DIALOG");
                break;
        }
    }
}
