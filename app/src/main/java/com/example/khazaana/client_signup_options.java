package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class client_signup_options extends AppCompatActivity {

    Button google_signup;
    Button email_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_signup_options);
        google_signup = findViewById(R.id.google_signup3);
        email_signup = findViewById(R.id.email_signup2);

        google_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        email_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(client_signup_options.this, client_registration.class);
                startActivity(intent);
            }
        });


    }
}