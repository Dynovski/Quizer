package project.android.course.quizer.firebaseObjects;

public class Course
{
    private String courseName;
    private String teacher;

    // Empty constructor needed by Firebase
    public Course() {}

    public Course(String courseName, String teacher)
    {
        this.courseName = courseName;
        this.teacher = teacher;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public String getTeacher()
    {
        return teacher;
    }
}
