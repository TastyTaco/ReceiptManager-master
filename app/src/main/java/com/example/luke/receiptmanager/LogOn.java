package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class LogOn extends Activity {

    FirebaseWrapper firebaseWrapper;
    //Firebase ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        firebaseWrapper = new FirebaseWrapper(getApplicationContext());
        //Firebase.setAndroidContext(this);

        //ref = new Firebase("https://reciptmanger.firebaseio.com");
        Button btnLogOn = (Button)findViewById(R.id.btnLogIn);

        final TextView txtEmailAddress = (TextView)findViewById(R.id.txtEmail);
        final TextView txtPassword = (TextView)findViewById(R.id.txtPassword);

        btnLogOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOn(txtEmailAddress.getText().toString(), txtPassword.getText().toString());
            }
        });
    }


    void LogOn(String emailAddress, String password){

        Firebase ref = firebaseWrapper.firebase; //new Firebase("https://reciptmanger.firebaseio.com");
        ref.authWithPassword(emailAddress, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent intent = new Intent(LogOn.this, HomeActivity.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}
