package project.android.course.quizer.singletons;

import project.android.course.quizer.firebaseObjects.User;

// Singleton containing information about currently logged in user
public class CurrentUser
{
    private static User INSTANCE;

    private CurrentUser() {}

    public static User getCurrentUser()
    {
        return INSTANCE;
    }

    public static void logInUser(User user)
    {
        if(INSTANCE == null)
            INSTANCE = user;
    }

    public static void logOutUser()
    {
        INSTANCE = null;
    }
}
