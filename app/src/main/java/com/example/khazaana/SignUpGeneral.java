package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpGeneral extends AppCompatActivity {

    private TextView back;
    private Button ifa;
    private Button client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_general);
        ifa = findViewById(R.id.financial_advisor);
        client = findViewById((R.id.client));
        back = findViewById(R.id.back);

        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        back.setTypeface(mont);

        back.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SignUpGeneral.this, LoadScreen.class);
                startActivity(intent);
            }
        });

        ifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpGeneral.this, sign_up_options.class);
                startActivity(intent);
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpGeneral.this, sign_up_options.class);
                startActivity(intent);
            }
        });
    }
}