package com.example.khazaana.main.ifa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;

public class IFAFees extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_fees);

        Button previousPage = findViewById(R.id.back_ifa_fees);
        previousPage.setOnClickListener(this::previousPage);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, IFA_AccountSettings.class);
        startActivity(intent);
    }
}