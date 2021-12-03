package com.example.khazaana.main.ifa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.khazaana.R;
import com.example.khazaana.main.ifa.IFASettings;

public class RemoveClient extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_client);

        Button previousPage = findViewById(R.id.back_remove_client);
        previousPage.setOnClickListener(this::previousPage);

    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, IFASettings.class);
        startActivity(intent);
    }
}
