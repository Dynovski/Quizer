package project.android.course.quizer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import project.android.course.quizer.R;
import project.android.course.quizer.fragments.AddCourseDialogFragment;
import project.android.course.quizer.singletons.CurrentUser;

public class TeacherHomeScreenActivity extends AppCompatActivity implements View.OnClickListener
{
    private Bundle userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_screen);

        userInfo = getIntent().getExtras();

        findViewById(R.id.sign_out_card).setOnClickListener(this);
        findViewById(R.id.my_account_card).setOnClickListener(this);
        findViewById(R.id.create_course_card).setOnClickListener(this);
        findViewById(R.id.teacher_courses_card).setOnClickListener(this);
        //findViewById(R.id.create_test_card).setOnClickListener(this);
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
            case R.id.create_test_card:
                startActivity(new Intent(this, CreateTestActivity.class));
                break;
        }
    }


}
