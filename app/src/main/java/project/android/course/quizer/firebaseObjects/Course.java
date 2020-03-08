package project.android.course.quizer.firebaseObjects;

// Conversion class for courses from database to java code and the other way
public class Course
{
    private String courseName;
    private String teacher;
    private String description;

    // Empty constructor needed by Firebase
    public Course() {}

    public Course(String courseName, String teacher, String description)
    {
        this.courseName = courseName;
        this.teacher = teacher;
        this.description = description;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public String getTeacher()
    {
        return teacher;
    }

    public String getDescription()
    {
        return description;
    }
}
