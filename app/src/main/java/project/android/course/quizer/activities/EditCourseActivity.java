package project.android.course.quizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import project.android.course.quizer.R;

// Activity used in startActivityForResult, it coordinates layout for editing course name and
// description, only providing name is compulsory, description can be empty
public class EditCourseActivity extends AppCompatActivity
{
    // Layout related variables
    private EditText courseName;
    private EditText courseDescription;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        courseName = findViewById(R.id.course_name_edit_text);
        courseDescription = findViewById(R.id.course_description_edit_text);
        editButton = findViewById(R.id.course_edit_button);

        Bundle courseInfo = getIntent().getExtras();
        String oldCourseName = courseInfo.getString("courseName");

        courseName.setText(oldCourseName);
        courseDescription.setText(courseInfo.getString("courseDescription"));

        editButton.setOnClickListener(v -> {
            Intent replyIntent = new Intent();
            String newCourseName = courseName.getText().toString().trim();
            if (newCourseName.isEmpty())
            {
                courseName.setError("Course must have a name");
                courseName.requestFocus();
            } else
            {
                replyIntent.putExtra("oldCourseName", oldCourseName);
                replyIntent.putExtra("newCourseName", newCourseName);
                String description = courseDescription.getText().toString().trim();
                replyIntent.putExtra("courseDescription", description);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}
