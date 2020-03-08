package project.android.course.quizer.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;

// Activity coordinating updating of account information after creating new account
// It makes it possible for user to choose a name, it has back button disabled because it is one time
// only event when user creates new account after that changes must be done directly in database
public class UpdateAccountActivity extends AppCompatActivity
{
    private static final String TAG = "UPDATE_ACCOUNT_DEBUG";

    // Firebase variables
    private FirebaseFirestore database;

    // Layout related variables
    private EditText nameEditText;
    private TextView emailTextView;
    private TextView userTypeTextView;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_accout);

        database = FirebaseFirestore.getInstance();

        nameEditText = findViewById(R.id.account_name_edit_text);
        emailTextView = findViewById(R.id.account_email_text_view);
        userTypeTextView = findViewById(R.id.account_user_type_text_view);
        saveButton = findViewById(R.id.account_save_button);

        nameEditText.setText(CurrentUser.getCurrentUser().getName());
        emailTextView.setText(CurrentUser.getCurrentUser().getEmail());
        userTypeTextView.setText(CurrentUser.getCurrentUser().getPrivilegeLevel() == 1 ? R.string.account_teacher : R.string.account_student);

        saveButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(nameEditText.getText()))
            {
                nameEditText.setError("Name field cannot be empty");
                nameEditText.requestFocus();
            }
            else
            {
                String accountName = nameEditText.getText().toString();
                // Update name in database
                database.collection("Users")
                        .document(CurrentUser.getCurrentUser().getUserId())
                        .update("name", accountName)
                        .addOnSuccessListener(aVoid -> {
                            // If update successful get new user info and replace it in singleton
                            database.collection("Users")
                                    .document(CurrentUser.getCurrentUser().getUserId()).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if(documentSnapshot.exists())
                                        {
                                            CurrentUser.logOutUser();
                                            CurrentUser.logInUser(documentSnapshot.toObject(User.class));
                                        }
                                        else
                                            Log.d(TAG, "Document does not exist");
                                        Log.d(TAG, "Successfully updated account information");
                                    });
                        })
                        .addOnFailureListener(e ->
                                Log.d(TAG, "Couldn't update account information " + e.toString()));
                Toast.makeText(getApplicationContext(), R.string.update_account_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        return;
    }
}
