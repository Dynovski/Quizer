package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.singletons.CurrentUser;


public class UserAccountActivity extends AppCompatActivity
{
    // Layout related variables
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mUserTypeTextView;

    // Firebase instance variables
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mNameTextView = findViewById(R.id.text_view_account_name);
        mEmailTextView = findViewById(R.id.text_view_account_email);
        mUserTypeTextView = findViewById(R.id.text_view_user_type);

        mNameTextView.setText(CurrentUser.getCurrentUser().getName());
        mEmailTextView.setText(CurrentUser.getCurrentUser().getEmail());
        mUserTypeTextView.setText(CurrentUser.getCurrentUser().getPrivilegeLevel() == 1 ? R.string.account_teacher : R.string.account_student);
    }
}
