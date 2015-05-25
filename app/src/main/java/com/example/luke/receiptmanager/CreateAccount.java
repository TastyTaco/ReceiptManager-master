package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by luke on 5/23/2015.
 */
public class CreateAccount extends Activity {

    FirebaseWrapper firebaseWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        firebaseWrapper = FirebaseWrapper.getInstance(getApplicationContext());

        Button btnCreateAccount = (Button)findViewById(R.id.btnRegister);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtEmailAddress = (TextView)findViewById(R.id.txtEmail);
                TextView txtPassword = (TextView)findViewById(R.id.txtPassword);

                String emailAddress = txtEmailAddress.getText().toString();
                String password = txtPassword.getText().toString();

                CreateUser(emailAddress, password);
            }
        });
    }

    void CreateUser(String emailAddress, String password){
        CreateUserEvent createUserEvent = new CreateUserEvent();
        firebaseWrapper.createUser(emailAddress, password, createUserEvent);
    }

    class CreateUserEvent implements Firebase.ValueResultHandler<Map<String, Object>> {
        public void onSuccess(Map<String, Object> result) {
            System.out.println("Successfully created user account with uid: " + result.get("uid"));
            firebaseWrapper.setUserId(result.get("uid").toString());

            Intent intent = new Intent(CreateAccount.this, HomeActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            // there was an error
            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }

}


