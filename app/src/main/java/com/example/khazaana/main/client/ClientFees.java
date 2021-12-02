package com.example.khazaana.main.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;
import com.example.khazaana.main.client.ClientAccountSettings;

public class ClientFees extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_fees);

        Button backButton = findViewById(R.id.back_client_fees);
        backButton.setOnClickListener(this::previousPage);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, ClientAccountSettings.class);
        startActivity(intent);
    }
}