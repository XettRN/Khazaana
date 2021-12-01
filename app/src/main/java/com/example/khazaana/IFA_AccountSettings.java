package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.main.Profile;

public class IFA_AccountSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_account_settings);

        Button profile = findViewById(R.id.ifa_profile);
        profile.setOnClickListener(this::ifaProfile);

        Button fees = findViewById(R.id.ifa_fees);
        fees.setOnClickListener(this::ifaFees);

        Button settings = findViewById(R.id.ifa_settings);
        settings.setOnClickListener(this::ifaSettings);
    }

    public void ifaProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void ifaFees(View view) {
        Intent intent = new Intent(this, IFAFees.class);
        startActivity(intent);
    }

    public void ifaSettings(View view) {
        Intent intent = new Intent(this, IFASettings.class);
        startActivity(intent);
    }

}