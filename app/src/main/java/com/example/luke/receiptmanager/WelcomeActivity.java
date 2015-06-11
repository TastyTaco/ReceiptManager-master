package com.example.luke.receiptmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupLoginButton();

        setupRegisterButton();
    }

    void setupLoginButton() {
        Button btnLogIn = (Button)findViewById(R.id.btnWelcomeLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch the logon intent.
                Intent intent = new Intent(WelcomeActivity.this, LogOn.class);
                startActivity(intent);
            }
        });
    }

    void setupRegisterButton() {
        Button btnRegister = (Button)findViewById(R.id.btnWelcomeRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch the register intent.
                Intent intent = new Intent(WelcomeActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }

}
