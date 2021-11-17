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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class client_registration extends AppCompatActivity {

    EditText fName, lName, email, pass, repass,ifa_key;
    Button nextScreen;
    FirebaseAuth fAuthorization;
    FirebaseFirestore fStore;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);

        fName = findViewById(R.id.firstName3);
        lName = findViewById(R.id.lastName2);
        email = findViewById(R.id.email2);
        pass = findViewById(R.id.passText2);
        repass = findViewById(R.id.reEnterPass2);
        ifa_key = findViewById(R.id.ifaKey);
        nextScreen = findViewById(R.id.next3);
        fAuthorization = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuthorization.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), userDetails.class));
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
                String ifaKey = ifa_key.getText().toString();

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

                if (TextUtils.isEmpty(ifaKey)){
                    ifa_key.setError("Please enter a valid ifa Key!!");
                    return;
                }


                fAuthorization.createUserWithEmailAndPassword(emailAdd, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(client_registration.this,"User Created", Toast.LENGTH_SHORT).show();

                            uID = fAuthorization.getCurrentUser().getUid();
                            DocumentReference d = fStore.collection("Authorized IFAs").document(ifaKey).collection("Clients").document(uID);
                            Map<String, Object> new_Client = new HashMap<>();
                            ArrayList<Integer> eq = new ArrayList<>();
                            eq.add(25);
                            eq.add(75);
                            new_Client.put("Equity", eq);
                            new_Client.put("First Name", firstName);
                            new_Client.put("Last Name", lastName);
                            DocumentReference dref = fStore.collection("Client List").document(uID);
                            Map<String, Object> client = new HashMap<>();
                            client.put("First Name", firstName);
                            client.put("Last Name", lastName);
                            client.put("Email", emailAdd);
                            d.set(new_Client);
                            dref.set(client);

                            startActivity(new Intent(getApplicationContext(), userDetails.class));

                        } else {
                            Toast.makeText(client_registration.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }
}