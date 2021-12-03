package com.example.khazaana.main.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;

public class ClientAccountSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_account_settings);
        Button profile = findViewById(R.id.client_profile);
        profile.setOnClickListener(this::goClientProfile);

        Button fees = findViewById(R.id.client_fees);
        fees.setOnClickListener(this::goClientFees);

        Button risk = findViewById(R.id.client_risk_profiling);
        risk.setOnClickListener(this::goRiskProfiling);

        Button settings = findViewById(R.id.client_settings);
        settings.setOnClickListener(this::goClientSettings);
    }

    public void goClientProfile(View view) {
        Intent intent = new Intent(this, ClientProfile.class);
        startActivity(intent);
    }

    public void goClientFees(View view) {
        Intent intent = new Intent(this, ClientFees.class);
        startActivity(intent);
    }

    public void goRiskProfiling(View view) {
        Intent intent = new Intent(this, ClientRiskProfilingMain.class);
        startActivity(intent);
    }

    public void goClientSettings(View view) {
        Intent intent = new Intent(this, ClientSettings.class);
        startActivity(intent);
    }
}