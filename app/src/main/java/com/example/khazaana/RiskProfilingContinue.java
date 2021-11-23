package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RiskProfilingContinue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_continue);

        Button skipPage = findViewById(R.id.next12);
        skipPage.setOnClickListener(this::skipPage);

        Button continuousPage = findViewById(R.id.next12);
        continuousPage.setOnClickListener(this::continuousPage);

        Button previousPage = findViewById(R.id.button16);
        previousPage.setOnClickListener(this::previousPage);
    }

    public void skipPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_12.class);
        startActivity(intent);
    }

    public void continuousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_12.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_12.class);
        startActivity(intent);
    }
}