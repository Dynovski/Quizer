package project.android.course.quizer.firebaseObjects;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

// Custom class making liveData from Firebase queries that can be observed by other parts of application
public class FirebaseQueryLiveData extends LiveData<QuerySnapshot>
{
    private static final String TAG = "FIREBASE_LIVEDATA_DEBUG";

    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    private ListenerRegistration registration;

    public FirebaseQueryLiveData(Query query)
    {
        this.query = query;
    }

    public FirebaseQueryLiveData(CollectionReference ref)
    {
        this.query = ref;
    }

    // Attaching listener
    @Override
    protected void onActive()
    {
        Log.d(TAG, "onActive");
        registration = query.addSnapshotListener(listener);
    }

    // Removing listener
    @Override
    protected void onInactive()
    {
        Log.d(TAG, "onInactive");
        registration.remove();
    }

    private class MyValueEventListener implements EventListener<QuerySnapshot>
    {
        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
        {
            if(e != null)
            {
                Log.d(TAG, "Listen failed: " + e);
                return;
            }

            for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges())
            {
                switch(dc.getType())
                {
                    case ADDED:
                        Log.d(TAG, "New document: " + dc.getDocument().getData());
                        setValue(queryDocumentSnapshots);
                        break;
                    case MODIFIED:
                        Log.d(TAG, "Modified document: " + dc.getDocument().getData());
                        setValue(queryDocumentSnapshots);
                        break;
                    case REMOVED:
                        Log.d(TAG, "Removed document: " + dc.getDocument().getData());
                        setValue(queryDocumentSnapshots);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}