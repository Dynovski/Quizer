package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import project.android.course.quizer.R;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating actions available in student's home screen, upon choosing one of the
// options it delegates user to new activities
public class StudentHomeScreenActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);

        findViewById(R.id.sign_out_card).setOnClickListener(this);
        findViewById(R.id.subscribed_courses_card).setOnClickListener(this);
        findViewById(R.id.list_of_courses_card).setOnClickListener(this);
        findViewById(R.id.my_account_card).setOnClickListener(this);
        findViewById(R.id.tests_to_do_card).setOnClickListener(this);
        findViewById(R.id.results_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.subscribed_courses_card:
                startActivity(new Intent(this, SubscribedCoursesActivity.class));
                break;
            case R.id.my_account_card:
                startActivity(new Intent(this, UserAccountActivity.class));
                break;
            case R.id.sign_out_card:
                CurrentUser.logOutUser();
                FirebaseAuth.getInstance().signOut();
                try
                {
                    Thread.sleep(500);
                } catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(intent);
                Runtime.getRuntime().exit(0);
                break;
            case R.id.list_of_courses_card:
                startActivity(new Intent(this, ListOfCoursesActivity.class));
                break;
            case R.id.tests_to_do_card:
                startActivity(new Intent(this, TestsToDoActivity.class));
                break;
            case R.id.results_card:
                startActivity(new Intent(this, ShowResultsActivity.class));
                break;
        }
    }
}
