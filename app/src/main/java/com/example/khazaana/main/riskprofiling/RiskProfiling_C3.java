package com.example.khazaana.main.riskprofiling;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class RiskProfiling_C3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_c3);

        Button nextPage = findViewById(R.id.next17);
        nextPage.setOnClickListener(this::nextPage);

        Button previousPage = findViewById(R.id.button15);
        previousPage.setOnClickListener(this::previousPage);

        ChipGroup choiceChipGroup = findViewById(R.id.choice_chip_group_c3);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {

                Log.i("RPC3", i + "");
                for (int j = 0; j < chipGroup.getChildCount(); j++) {
                    Chip c = (Chip) chipGroup.getChildAt(j);
                    Log.d("TAG", "Chip Selected :" +c.getText());
                    RiskProfilingValues.setRisk_profiling_valueC3(String.valueOf(c.getText()));
                }
            }
        });
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_Answer.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_C2.class);
        startActivity(intent);
    }
}