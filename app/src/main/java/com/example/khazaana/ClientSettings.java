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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ClientSettings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button addifa = findViewById(R.id.add_ifa);
        addifa.setOnClickListener(this::goAddIFA);

        Button removeifa = findViewById(R.id.remove_ifa);
        removeifa.setOnClickListener(this::goRemoveIFA);

        Button updatePassword = findViewById(R.id.update_password);
        updatePassword.setOnClickListener(this::goUpdatePassword);


    }

    public void goAddIFA(View view) {
        Intent intent = new Intent(this, AddNewIFA.class);
        startActivity(intent);
    }

    public void goRemoveIFA(View view) {
        Intent intent = new Intent(this, RemoveIFA.class);
        startActivity(intent);
    }

    public void goUpdatePassword(View view) {
        Intent intent = new Intent(this, UpdatePassword.class);
        startActivity(intent);
    }
}