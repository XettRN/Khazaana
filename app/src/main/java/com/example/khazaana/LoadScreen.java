package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadScreen extends AppCompatActivity {

    private TextView title;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);
        title = findViewById(R.id.loadScreen);
        signIn = findViewById((R.id.sign_in));
        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        title.setTypeface(mont);

        signIn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoadScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });
    }
}