package com.example.luke.receiptmanager;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by luke on 5/23/2015.
 */
public class CreateAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    void CreateUser(String EmailAddress, String UserName){

        Firebase ref = new Firebase("https://reciptmanger.firebaseio.com");
        ref.createUser(EmailAddress, UserName, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}


