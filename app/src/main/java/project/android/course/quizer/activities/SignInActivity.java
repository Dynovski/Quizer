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

public class SignInActivity extends BaseSignActionActivity implements View.OnClickListener
{
    private static final String TAG = "SIGN_IN_DEBUG";
    static final int RESET_PASSWORD_REQUEST = 1;

    // Layout related variables
    private EditText mEmailField;
    private EditText mPasswordField;

    // Firebase instance variables
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Connecting layout variables with code
        mEmailField = findViewById(R.id.edit_text_email);
        mPasswordField = findViewById(R.id.edit_text_password);
        setProgressBar(R.id.progressbar);

        // Get Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        findViewById(R.id.button_sign_in).setOnClickListener(this);
        findViewById(R.id.text_view_register_delegation).setOnClickListener(this);
        findViewById(R.id.text_view_forget_password).setOnClickListener(this);
    }

    // User still logged in
    @Override
    protected void onStart()
    {
        super.onStart();

        if(mAuth.getCurrentUser() != null)
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
            case R.id.text_view_register_delegation:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.button_sign_in:
                login();
                break;
            case R.id.text_view_forget_password:
                startActivityForResult(new Intent(this, ResetPasswordActivity.class),
                        RESET_PASSWORD_REQUEST);
                break;
        }
    }

    private void login()
    {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        Log.d(TAG, "login of " + email);

        if(!dataValid())
            return;

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
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
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.collection("Users").document(userId).get()
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
}
