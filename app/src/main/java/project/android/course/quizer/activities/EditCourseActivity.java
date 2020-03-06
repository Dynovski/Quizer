package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import project.android.course.quizer.R;

public class EditCourseActivity extends AppCompatActivity
{
    private EditText courseName;
    private EditText courseDescription;
    private Button editButton;
    private String oldCourseName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        courseName = findViewById(R.id.course_name_edit_text);
        courseDescription = findViewById(R.id.course_description_edit_text);
        editButton = findViewById(R.id.course_edit_button);

        Bundle courseInfo = getIntent().getExtras();

        oldCourseName = courseInfo.getString("courseName");
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
    //TODO: PO edycji nazwy trzeba bedzie aktualizować subscribed courses i completed tests
}
