package com.example.luke.receiptmanager;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Logan Mabbett on 15/05/2015.
 */
public class FirebaseWrapper {
    final String firebaseUrl = "https://reciptmanger.firebaseio.com/";

    static FirebaseWrapper firebaseWrapper;
    Firebase firebase;

    public String userId;

    private String loadedData;

    private FirebaseWrapper(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(firebaseUrl);
    }

    public static FirebaseWrapper getInstance(Context context) {
        if (firebaseWrapper == null)
            firebaseWrapper = new FirebaseWrapper(context);

        return firebaseWrapper;
    }

    public void saveToFirebase(String userName, String jsonString) {
        Firebase usersFileLocation = firebase.child("Users").child(userName);
        Firebase updatedUsersFileLocation = usersFileLocation.push();
        updatedUsersFileLocation.setValue(jsonString);
    }

    public String loadFromFirebase(String userName) {
        Firebase usersFileLocation = firebase.child("Users").child(userName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadedData = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        usersFileLocation.addListenerForSingleValueEvent(valueEventListener);

        usersFileLocation.removeEventListener(valueEventListener);

        return loadedData;
    }

    public void SetUserId(String userId)
    {
       this.userId = userId;
    }

    //https://www.firebase.com/docs/android/guide/saving-data.html

    //https://www.firebase.com/docs/android/guide/retrieving-data.html
}
