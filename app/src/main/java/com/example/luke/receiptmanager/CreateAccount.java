package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by luke on 5/23/2015.
 */
public class CreateAccount extends Activity {

    ProgressBar spinner;

    FirebaseWrapper firebaseWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        //Get a singleton instance of the firebase wrapper.
        firebaseWrapper = FirebaseWrapper.getInstance(getApplicationContext());

        setupCreateAccountButton();

        spinner = (ProgressBar)findViewById(R.id.spnrRegister);
        spinner.setVisibility(View.GONE); //Hide spinner on load.
    }

    void setupCreateAccountButton() {
        Button btnCreateAccount = (Button)findViewById(R.id.btnRegister);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtEmailAddress = (TextView)findViewById(R.id.txtEmail);
                TextView txtPassword = (TextView)findViewById(R.id.txtPassword);

                //Load email and password from interface.
                String emailAddress = txtEmailAddress.getText().toString();
                String password = txtPassword.getText().toString();

                //Register a user.
                CreateUser(emailAddress, password);
            }
        });
    }

    void CreateUser(String emailAddress, String password) {
        //Show the spinner while the user is being registered.
        spinner.setVisibility(View.VISIBLE);
        CreateUserEvent createUserEvent = new CreateUserEvent();

        //Create the user account, passing in the event to return behaviour from this form.
        firebaseWrapper.createUser(emailAddress, password, createUserEvent);
    }

    class CreateUserEvent implements Firebase.ValueResultHandler<Map<String, Object>> {
        ProgressBar spinner = (ProgressBar)findViewById(R.id.spnrRegister);

        public void onSuccess(Map<String, Object> result) {
            System.out.println("Successfully created user account with uid: " + result.get("uid"));
            //Set the userId for firebase to use.
            firebaseWrapper.setUserId(result.get("uid").toString());

            //Hide the spinnter
            spinner.setVisibility(View.GONE);

            //Launch the home activity.
            Intent intent = new Intent(CreateAccount.this, HomeActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            // there was an error
            spinner.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }

}


