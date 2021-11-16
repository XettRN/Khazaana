package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RiskProfiling_1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_1);

        Button nextPage = findViewById(R.id.next2);
        nextPage.setOnClickListener(this::nextPage);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_2.class);
        startActivity(intent);
    }
}