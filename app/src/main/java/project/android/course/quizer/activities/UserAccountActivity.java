package project.android.course.quizer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import project.android.course.quizer.R;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating account information display
public class UserAccountActivity extends AppCompatActivity
{
    // Layout related variables
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView userTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        nameTextView = findViewById(R.id.account_name_text_view);
        emailTextView = findViewById(R.id.account_email_text_view);
        userTypeTextView = findViewById(R.id.account_user_type_text_view);

        nameTextView.setText(CurrentUser.getCurrentUser().getName());
        emailTextView.setText(CurrentUser.getCurrentUser().getEmail());
        userTypeTextView.setText(CurrentUser.getCurrentUser().getPrivilegeLevel() == 1 ? R.string.account_teacher : R.string.account_student);
    }
}
