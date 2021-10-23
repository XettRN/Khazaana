package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class IFARegistration extends AppCompatActivity {

    EditText fName, lName, email, pass, repass;
    Button nextScreen;
    FirebaseAuth fAuthorization;
    FirebaseFirestore fStore;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifaregistration);

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.passText);
        repass = findViewById(R.id.reEnterPass);
        nextScreen = findViewById(R.id.next);
        fAuthorization = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuthorization.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), ifaClientRegistrationDetails.class));
            finish();
        }



        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                String emailAdd = email.getText().toString();
                String password = pass.getText().toString();
                String passMatch = repass.getText().toString();

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

                if (TextUtils.isEmpty(password)){
                    pass.setError("Please enter a valid password!!");
                    return;
                }

                if (password.length() < 6) {
                    pass.setError("Password can only be >= 6 characters");
                    return;
                }

                if (!TextUtils.equals(password, passMatch)) {
                    repass.setError("Password does not match!!");
                    return;
                }

                fAuthorization.createUserWithEmailAndPassword(emailAdd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(IFARegistration.this,"User Created", Toast.LENGTH_SHORT).show();
                            uID = fAuthorization.getCurrentUser().getUid();
                            DocumentReference d = fStore.collection("Authorized IFAs").document(uID);

                            Map<String, Object> authorizedUsers = new HashMap<>();

                            authorizedUsers.put("First Name", firstName);
                            authorizedUsers.put("Last Name", lastName);
                            authorizedUsers.put("Email Address", emailAdd);

                            d.set(authorizedUsers);
                            startActivity(new Intent(getApplicationContext(), ifaClientRegistrationDetails.class));

                        } else {
                            Toast.makeText(IFARegistration.this,"User creation unsuccessful!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }

    public String getFirst() {
        return fName.getText().toString();
    }

    public String getLast() {
        return lName.getText().toString();
    }
}