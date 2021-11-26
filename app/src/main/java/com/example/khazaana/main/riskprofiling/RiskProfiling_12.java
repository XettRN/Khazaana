package com.example.khazaana.main.riskprofiling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;

public class RiskProfiling_12 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_12);

        Button nextPage = findViewById(R.id.next14);
        nextPage.setOnClickListener(this::nextPage);

        Button previousPage = findViewById(R.id.button12);
        previousPage.setOnClickListener(this::previousPage);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, RiskProfilingContinue.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_11.class);
        startActivity(intent);
    }
}