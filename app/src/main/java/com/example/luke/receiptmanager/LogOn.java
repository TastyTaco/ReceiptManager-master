package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class LogOn extends Activity {

    ProgressBar spinner;

    FirebaseWrapper firebaseWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);

        //Get an instance of firebase wrapper.
        firebaseWrapper = FirebaseWrapper.getInstance(getApplicationContext());

        setupLogonButton();

        spinner = (ProgressBar)findViewById(R.id.spnrLogon);
        spinner.setVisibility(View.GONE); //Hide the spinner.
    }

    void setupLogonButton() {
        Button btnLogOn = (Button)findViewById(R.id.btnLogIn);
        btnLogOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtEmailAddress = (TextView)findViewById(R.id.txtEmail);
                TextView txtPassword = (TextView)findViewById(R.id.txtPassword);

                //Grab the data out of the interface.
                String emailAddress = txtEmailAddress.getText().toString();
                String password =  txtPassword.getText().toString();

                LogOn(emailAddress, password);
            }
        });
    }

    void LogOn(String emailAddress, String password){
        //Show the progress bar.
        spinner.setVisibility(View.VISIBLE);

        //Setup the event to process data from this activity.
        LoginEvent loginEvent = new LoginEvent();

        //Authenticate the user with firebase.
        firebaseWrapper.authWithPassword(emailAddress, password, loginEvent);
    }

    class LoginEvent implements Firebase.AuthResultHandler {
        ProgressBar spinner = (ProgressBar)findViewById(R.id.spnrLogon);

        @Override
        public void onAuthenticated(AuthData authData) {
            System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            firebaseWrapper.setUserId(authData.getUid());

            //Hide the spinner.
            spinner.setVisibility(View.GONE);

            //Start the home activity after log on.
            Intent intent = new Intent(LogOn.this, HomeActivity.class);
            startActivity(intent);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // there was an error
            spinner.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Failed to Authenticate", Toast.LENGTH_LONG).show();
        }
    }
}
