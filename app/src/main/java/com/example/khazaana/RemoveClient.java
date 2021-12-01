package com.example.khazaana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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
