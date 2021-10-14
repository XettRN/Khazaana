package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {

    EditText email;
    EditText password;

    ImageView signin;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        signin = findViewById(R.id.sign_in);
        fAuth = FirebaseAuth.getInstance();

        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.otf");
        email.setTypeface(mont);
        password.setTypeface(mont);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAdd = email.getText().toString();
                String pass = password.getText().toString();


                /*Matches the user credentials to that stored in database*/
                fAuth.signInWithEmailAndPassword(emailAdd, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginScreen.this,"Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ifaClientRegistrationDetails.class));
                        } else {
                            Toast.makeText(LoginScreen.this,"Error!!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}