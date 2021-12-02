package com.example.khazaana;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewClient extends AppCompatActivity {
    EditText fName, lName, email;
    Button addClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_client);
        fName = findViewById(R.id.first_name_input);
        lName = findViewById(R.id.last_name_input2);
        email = findViewById(R.id.email_input);
        addClient = findViewById(R.id.addClient);

        Button previousPage = findViewById(R.id.back_add_new_client);
        previousPage.setOnClickListener(this::previousPage);

        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String emailAdd = email.getText().toString();


                if (TextUtils.isEmpty(firstName)) {
                    fName.setError("Please enter a first name!!");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    lName.setError("Please enter a last name!!");
                    return;
                }

                if (TextUtils.isEmpty(emailAdd)){
                    email.setError("Please enter a valid email address!!");
                    return;
                }


            }
        });

    }

    public void previousPage(View view) {
        Intent intent = new Intent(this, IFASettings.class);
        startActivity(intent);
    }
}
