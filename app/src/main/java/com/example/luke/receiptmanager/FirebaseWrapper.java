package com.example.luke.receiptmanager;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Logan Mabbett on 15/05/2015.
 */
public class FirebaseWrapper {
    final String firebaseUrl = "https://reciptmanger.firebaseio.com/";

    private static FirebaseWrapper firebaseWrapper;
    private Firebase firebase;

    private String userId;

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

    public void saveToFirebase(String jsonString) {
        if (userId == null) return;
        Firebase usersFileLocation = firebase.child("Users").child(userId);
        Firebase updatedUsersFileLocation = usersFileLocation.push();
        updatedUsersFileLocation.setValue(jsonString);
    }

    public String loadFromFirebase() {
        if (userId == null) return "";

        Firebase usersFileLocation = firebase.child("Users").child(userId);
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

    public void setUserId(String userId)
    {
       this.userId = userId;
    }

    public String getUserId() { return userId; }

    public void createUser(String emailAddress, String password, Firebase.ValueResultHandler<Map<String, Object>> resultHandler) {
        firebase.createUser(emailAddress, password, resultHandler);
    }

    public void authWithPassword(String emailAddress, String password, Firebase.AuthResultHandler authResultHandler) {
        firebase.authWithPassword(emailAddress, password, authResultHandler);
    }

    //https://www.firebase.com/docs/android/guide/saving-data.html

    //https://www.firebase.com/docs/android/guide/retrieving-data.html
}
