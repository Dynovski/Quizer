package project.android.course.quizer.firebaseObjects;

// Conversion class for users from database to java code and the other way
public class User
{
    private int privilegeLevel;
    private String name;
    private String userId;
    private String email;

    // Empty constructor needed by Firebase
    public User() {}

    public User(int privilegeLevel, String name, String userId, String email)
    {
        this.privilegeLevel = privilegeLevel;
        this.name = name;
        this.userId = userId;
        this.email = email;
    }

    public int getPrivilegeLevel()
    {
        return privilegeLevel;
    }

    public String getName()
    {
        return name;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getEmail()
    {
        return email;
    }
}
