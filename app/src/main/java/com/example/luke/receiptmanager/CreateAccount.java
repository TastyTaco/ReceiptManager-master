package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by luke on 5/23/2015.
 */
public class CreateAccount extends Activity {

    //Firebase ref;
    FirebaseWrapper firebaseWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        firebaseWrapper = new FirebaseWrapper(getApplicationContext());
        //Firebase.setAndroidContext(this);

        //ref = new Firebase("https://reciptmanger.firebaseio.com");
        Button btnCreateAccount = (Button)findViewById(R.id.btnRegister);

        final TextView txtEmailAddress = (TextView)findViewById(R.id.txtEmail);
        final TextView txtPassword = (TextView)findViewById(R.id.txtPassword);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser(txtEmailAddress.getText().toString(), txtPassword.getText().toString());
            }
        });
    }

    void CreateUser(String emailAddress, String password){
        Firebase firebase = firebaseWrapper.firebase;
        firebase.createUser(emailAddress, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Intent intent = new Intent(CreateAccount.this, HomeActivity.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

}


