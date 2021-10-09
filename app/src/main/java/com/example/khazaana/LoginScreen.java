package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private TextView newuser;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        newuser = findViewById(R.id.newuser);
        signup = findViewById(R.id.signup);

        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.otf");
        email.setTypeface(mont);
        password.setTypeface(mont);
        newuser.setTypeface(mont);
        signup.setTypeface(mont);
    }
}