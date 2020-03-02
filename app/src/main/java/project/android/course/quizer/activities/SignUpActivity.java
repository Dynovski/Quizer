package project.android.course.quizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.activities.BaseSignActionActivity;
import project.android.course.quizer.activities.SignInActivity;
import project.android.course.quizer.firebaseObjects.User;

public class SignUpActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "SignUpLogging";

    // Layout related variables
    private EditText mEmailField;
    private EditText mPasswordField;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Connecting layout variables with code
        mEmailField = findViewById(R.id.edit_text_email);
        mPasswordField = findViewById(R.id.edit_text_password);
        setProgressBar(R.id.progressbar);

        // Get Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.text_view_login_delegation).setOnClickListener(this);
    }

    private void createAccount()
    {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        Log.d(TAG, "createAccount for " + email);

        if(!dataValid())
            return;

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "User successfully created");
                        // Adding new user to database with lowest privilege
                        String userId = mAuth.getCurrentUser().getUid();
                        mDatabase.collection("Users").document(userId).set(new User(2,
                                "", mAuth.getCurrentUser().getEmail()));
                        finish();
                        // Start new activity for a user
                        startActivity(new Intent(this, StudentHomeScreenActivity.class));
                    } else
                    {
                        Log.w(TAG, "User creation failed", task.getException());
                        if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Couldn't create new account", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressBar();
                });

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

        String password = mPasswordField.getText().toString().trim();
        if(password.isEmpty())
        {
            mPasswordField.setError("Password is required");
            mPasswordField.requestFocus();
            valid = false;
        }
        if(password.length() < 6)
        {
            mPasswordField.setError("Password must be at least 6 characters");
            mPasswordField.requestFocus();
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_sign_up:
                createAccount();
                break;
            case R.id.text_view_login_delegation:
                finish();
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }
}
