package project.android.course.quizer.firebaseObjects;

public class User
{
    private int privilegeLevel;
    private String name;
    private String email;

    // Empty constructor needed by Firebase
    public User() {}

    public User(int privilegeLevel, String name, String email)
    {
        this.privilegeLevel = privilegeLevel;
        this.name = name;
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

    public String getEmail()
    {
        return email;
    }
}
