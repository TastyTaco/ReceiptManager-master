package com.example.luke.receiptmanager;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by Logan Mabbett on 15/05/2015.
 */
public class FirebaseWrapper {

    final String firebaseUrl = "https://reciptmanger.firebaseio.com/";

    Firebase firebase;

    public FirebaseWrapper(Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase(firebaseUrl);
    }


    //https://www.firebase.com/docs/android/guide/saving-data.html

    //https://www.firebase.com/docs/android/guide/retrieving-data.html
}
