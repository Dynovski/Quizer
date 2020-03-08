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
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating user signing up, it checks the data that user entered and is creating new
// user account, it creates account with lowest privilege level for security reasons , if user needs
// higher one it needs to be changed in database by administrator, then in logs user in
public class SignUpActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "SIGN_UP_DEBUG";

    // Layout related variables
    private EditText emailEditText;
    private EditText passwordEditText;

    // Firebase instance variables
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Connecting layout variables with code
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        setProgressBar(R.id.progressbar);

        // Get Firebase instances
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Adding actions to clickable elements
        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.login_text_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.sign_up_button:
                hideKeyboard(view);
                createAccount();
                break;
            case R.id.login_text_view:
                finish();
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }

    private void createAccount()
    {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "createAccount for " + email);

        if(!dataValid())
            return;

        showProgressBar();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "User successfully created");
                        // Adding new user to database with lowest privilege and no name
                        String userId = auth.getCurrentUser().getUid();
                        String userEmail = auth.getCurrentUser().getEmail();
                        User newUser = new User(2, "", userId, userEmail);
                        CurrentUser.logInUser(newUser);
                        database.collection("Users").document(userId).set(newUser);
                        finish();
                        startActivity(new Intent(this, StudentHomeScreenActivity.class));
                        startActivity(new Intent(this, UpdateAccountActivity.class));
                    } else
                    {
                        Log.w(TAG, "User creation failed", task.getException());
                        if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            Toast.makeText(getApplicationContext(), R.string.sign_up_already_registered,
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), R.string.sign_up_failed,
                                    Toast.LENGTH_SHORT).show();
                    }
                    hideProgressBar();
                });

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

        String password = passwordEditText.getText().toString().trim();
        if(password.isEmpty())
        {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            valid = false;
        }

        if(password.length() < 6)
        {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            valid = false;
        }

        return valid;
    }
}
