package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khazaana.main.riskprofiling.RiskProfiling_1;
import com.example.khazaana.main.riskprofiling.RiskProfiling_C1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ClientRiskProfilingMain extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_main);

        Button rp1 = findViewById(R.id.risk_profiling1);
        rp1.setOnClickListener(this::rp1);

        Button rp2 = findViewById(R.id.risk_profiling2);
        rp2.setOnClickListener(this::rp2);
    }

    public void rp1(View view) {
        Intent intent = new Intent(this, RiskProfiling_1.class);
        startActivity(intent);
    }

    public void rp2(View view) {
        Intent intent = new Intent(this, RiskProfiling_C1.class);
        startActivity(intent);
    }
}