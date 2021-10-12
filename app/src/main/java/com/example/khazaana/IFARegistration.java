package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IFARegistration extends AppCompatActivity {

    TextView firstName, lastName, emailAddress, password, reEnterPass;
    Button nextScreen;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_registration);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.passText);
        reEnterPass = findViewById(R.id.reEnterPass);
        nextScreen = findViewById(R.id.next);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoadScreen.class));
            finish();
        }

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddress.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String pass = password.getText().toString();
                String rPass = reEnterPass.getText().toString();

                if (TextUtils.isEmpty(fName)) {
                    firstName.setError("Please enter a first name");
                    return;
                }

                if (TextUtils.isEmpty(lName)) {
                    lastName.setError("Please enter a last name");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    emailAddress.setError("Please enter a valid email address");
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    password.setError("Please enter a valid password");
                    return;
                }

                if (TextUtils.isEmpty(rPass)){
                    password.setError("Re-entered password does not match");
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Password can only be >= 6 characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(IFARegistration.this,"User Created", Toast.LENGTH_SHORT).show();

                            /*uID = fAuth.getCurrentUser().getUid();
                            DocumentReference dRef = fStore.collection("Authorized IFAs").document(uID);

                            Map<String, Object> authorizedIFAs = new HashMap<>();

                            authorizedIFAs.put("First Name", fName);
                            authorizedIFAs.put("Last Name", lName);
                            authorizedIFAs.put("Email Address", email);

                            dRef.set(authorizedIFAs);*/

                            uID = fAuth.getCurrentUser().getUid();
                            DocumentReference dRef = fStore.collection("Authorized Users").document(uID);

                            Map<String, Object> authorizedUsers = new HashMap<>();

                            authorizedUsers.put("First Name", fName);
                            authorizedUsers.put("Last Name", lName);
                            authorizedUsers.put("Email Address", email);

                            dRef.set(authorizedUsers);

                        } else {
                            Toast.makeText(IFARegistration.this,"Error!!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}