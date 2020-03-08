package project.android.course.quizer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import project.android.course.quizer.R;

// Activity coordinating password reset functionality, reset password email is sent to provided
// email address
public class ResetPasswordActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "RESET_PASSWORD_DEBUG";

    // Layout related variables
    private EditText emailEditText;

    // Firebase variables
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText = findViewById(R.id.email_edit_text);
        setProgressBar(R.id.progressbar);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.reset_password_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.reset_password_button:
                if(resetPassword())
                {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                break;
        }
    }

    private boolean resetPassword()
    {
        String email = emailEditText.getText().toString().trim();

        Log.d(TAG, "reset password for " + email);

        if(!dataValid())
            return false;

        showProgressBar();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "Reset password email sent");
                        finish();
                        startActivity(new Intent(this, SignInActivity.class));
                    } else
                    {
                        Log.w(TAG, "Reset email creation failed", task.getException());
                        Toast.makeText(getApplicationContext(), "Couldn't reset password", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressBar();
                });
        return true;
    }

    private boolean dataValid()
    {
        boolean valid = true;

        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            valid = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            valid = false;
        }

        return valid;
    }
}
