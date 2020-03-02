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

public class ResetPasswordActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "ResetPasswordLogging";

    // Layout related variables
    private EditText mEmailField;

    // Firebase instance variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Connecting layout variables with code
        mEmailField = findViewById(R.id.edit_text_email);
        setProgressBar(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_reset_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_reset_password:
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
        String email = mEmailField.getText().toString().trim();

        Log.d(TAG, "reset password for " + email);

        if(!dataValid())
            return false;

        showProgressBar();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "Reset password email sent");
                        finish();
                        startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class));
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

        String email = mEmailField.getText().toString().trim();
        if(email.isEmpty())
        {
            mEmailField.setError("Email is required");
            mEmailField.requestFocus();
            valid = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmailField.setError("Please enter a valid email");
            mEmailField.requestFocus();
            valid = false;
        }

        return valid;
    }
}
