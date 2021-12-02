package com.example.khazaana.main.ifa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.khazaana.R;

public class IFAUpdatePassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_update_password);

        Button previousPage = findViewById(R.id.back_ifa_update_password);
        previousPage.setOnClickListener(this::previousPage);

    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, IFASettings.class);
        startActivity(intent);
    }
}