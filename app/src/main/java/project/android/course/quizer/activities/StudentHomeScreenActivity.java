package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import project.android.course.quizer.R;
import project.android.course.quizer.singletons.CurrentUser;

public class StudentHomeScreenActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);

        findViewById(R.id.sign_out_card).setOnClickListener(this);
        //findViewById(R.id.student_courses_card).setOnClickListener(this);
        findViewById(R.id.all_courses_card).setOnClickListener(this);
        findViewById(R.id.my_account_card).setOnClickListener(this);
        //findViewById(R.id.tests_to_do_card).setOnClickListener(this);
        //findViewById(R.id.results_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.student_courses_card:
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
            case R.id.all_courses_card:
                startActivity(new Intent(this, AllCoursesActivity.class));
                break;
            case R.id.tests_to_do_card:
                break;
            case R.id.results_card:
                break;
        }
    }
}
