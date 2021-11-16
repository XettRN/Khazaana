package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RiskProfiling_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_3);

        Button nextPage = findViewById(R.id.next4);
        nextPage.setOnClickListener(this::nextPage);

        Button previousPage = findViewById(R.id.button3);
        previousPage.setOnClickListener(this::previousPage);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_4.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_2.class);
        startActivity(intent);
    }
}