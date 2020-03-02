package project.android.course.quizer.singletons;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import project.android.course.quizer.firebaseObjects.User;

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
