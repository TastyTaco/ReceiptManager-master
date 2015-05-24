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

    Firebase firebase;

    private String loadedData;

    public FirebaseWrapper(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(firebaseUrl);
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

    //https://www.firebase.com/docs/android/guide/saving-data.html

    //https://www.firebase.com/docs/android/guide/retrieving-data.html
}
