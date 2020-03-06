package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.R;
import project.android.course.quizer.firebaseObjects.User;
import project.android.course.quizer.singletons.CurrentUser;

public class UpdateAccountActivity extends AppCompatActivity
{
    private static final String TAG = "UPDATE_ACCOUNT_DEBUG";

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

        nameEditText = findViewById(R.id.account_name_edit_text);
        emailTextView = findViewById(R.id.account_email_text_view);
        userTypeTextView = findViewById(R.id.account_user_type_text_view);
        saveButton = findViewById(R.id.account_save_button);

        nameEditText.setText(CurrentUser.getCurrentUser().getName());
        emailTextView.setText(CurrentUser.getCurrentUser().getEmail());
        userTypeTextView.setText(CurrentUser.getCurrentUser().getPrivilegeLevel() == 1 ? R.string.account_teacher : R.string.account_student);

        saveButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(nameEditText.getText()))
            {
                nameEditText.setError("Name field cannot be empty");
                nameEditText.requestFocus();
            }
            else
            {
                String accountName = nameEditText.getText().toString();
                FirebaseFirestore.getInstance().collection("Users").document(CurrentUser.getCurrentUser().getUserId())
                        .update("name", accountName)
                        .addOnSuccessListener(aVoid -> {
                            FirebaseFirestore.getInstance().collection("Users").document(CurrentUser.getCurrentUser().getUserId()).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if(documentSnapshot.exists())
                                        {
                                            CurrentUser.logOutUser();
                                            CurrentUser.logInUser(documentSnapshot.toObject(User.class));
                                        }
                                        else
                                            Log.d(TAG, "Document does not exist");
                                        Log.d(TAG, "onActivityResult: Successfully updated account information");
                                    });
                        })
                        .addOnFailureListener(e -> Log.d(TAG, "onActivityResult: Couldn't update account information " + e.toString()));


                Toast.makeText(getApplicationContext(), "Account information updated", Toast.LENGTH_SHORT).show();
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
