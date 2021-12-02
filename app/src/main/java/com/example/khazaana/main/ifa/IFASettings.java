package com.example.khazaana.main.ifa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.khazaana.R;

public class IFASettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_settings);
        Button profile = findViewById(R.id.add_client);
        profile.setOnClickListener(this::goAddClient);

        Button fees = findViewById(R.id.remove_client);
        fees.setOnClickListener(this::goRemoveClient);

        Button risk = findViewById(R.id.update_password);
        risk.setOnClickListener(this::goUpdatePassword);

        Button backButton = findViewById(R.id.back_ifa_settings);
        backButton.setOnClickListener(this::previousPage);
    }

    public void goAddClient(View view) {
        Intent intent = new Intent(this, AddNewClient.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, IFA_AccountSettings.class);
        startActivity(intent);
    }

    public void goRemoveClient(View view) {
        Intent intent = new Intent(this, RemoveClient.class);
        startActivity(intent);
    }

    public void goUpdatePassword(View view) {
        Intent intent = new Intent(this, IFAUpdatePassword.class);
        startActivity(intent);
    }

}