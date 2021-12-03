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

public class RiskProfiling_8 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_8);

        Button nextPage = findViewById(R.id.next10);
        nextPage.setOnClickListener(this::nextPage);

        Button previousPage = findViewById(R.id.button8);
        previousPage.setOnClickListener(this::previousPage);

        ChipGroup choiceChipGroup = findViewById(R.id.choice_chip_group_8);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {

                Log.i("RP8", i + "");
                for (int j = 0; j < chipGroup.getChildCount(); j++) {
                    Chip c = (Chip) chipGroup.getChildAt(j);
                    Log.d("TAG", "Chip Selected :" +c.getText());
                    RiskProfilingValues.setRisk_profiling_value8(String.valueOf(c.getText()));
                }
            }
        });
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_9.class);
        startActivity(intent);
    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, RiskProfiling_7.class);
        startActivity(intent);
    }
}
