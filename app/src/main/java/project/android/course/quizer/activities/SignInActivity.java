package project.android.course.quizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating user signing in, it checks the data that user entered and is logging user
// to the view dependent on user privilege level
public class SignInActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "SIGN_IN_DEBUG";
    static final int RESET_PASSWORD_REQUEST = 1;

    // Layout related variables
    private EditText emailEditText;
    private EditText passwordEditText;

    // Firebase instance variables
    private FirebaseFirestore database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Connecting layout variables with code
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        setProgressBar(R.id.progressbar);

        // Get Firebase instances
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Adding actions to clickable elements
        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.text_view_register).setOnClickListener(this);
        findViewById(R.id.text_view_forgot_password).setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // User still logged in
        if(auth.getCurrentUser() != null)
        {
            loginUserToCorrespondingView();
            finish();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.text_view_register:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.button_sign_in:
                hideKeyboard(view);
                login();
                break;
            case R.id.text_view_forgot_password:
                startActivityForResult(new Intent(this, ResetPasswordActivity.class),
                        RESET_PASSWORD_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESET_PASSWORD_REQUEST)
        {
            if(resultCode == RESULT_OK)
                Toast.makeText(this, "Email pending...", Toast.LENGTH_SHORT).show();
            else if(resultCode == RESULT_CANCELED)
                Toast.makeText(this, "Cancelled password reset", Toast.LENGTH_SHORT).show();
        }
    }

    private void login()
    {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "login of " + email);

        if(!dataValid())
            return;

        showProgressBar();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d(TAG, "Login successful");
                        loginUserToCorrespondingView();
                        finish();
                    } else
                    {
                        Log.d(TAG, "Login failed", task.getException());
                        Toast.makeText(SignInActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
                    }
                    hideProgressBar();
                });
    }

    private void loginUserToCorrespondingView()
    {
        String userId = auth.getCurrentUser().getUid();
        database.collection("Users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists())
                    {
                        CurrentUser.logInUser(documentSnapshot.toObject(User.class));
                        switch(CurrentUser.getCurrentUser().getPrivilegeLevel())
                        {
                            case 1:
                                Intent teacherIntent = new Intent(SignInActivity.this, TeacherHomeScreenActivity.class);
                                teacherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(teacherIntent);
                                break;
                            case 2:
                                Intent studentIntent = new Intent(SignInActivity.this, StudentHomeScreenActivity.class);
                                studentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(studentIntent);
                                break;
                            default:
                                Log.d(TAG, "Illegal privilege value");
                        }
                    } else
                    {
                        Toast.makeText(SignInActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignInActivity.this, "Could not load read document", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
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
