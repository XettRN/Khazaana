package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khazaana.main.ClientBasic;
import com.example.khazaana.main.NavView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginScreen extends AppCompatActivity {

    EditText email;
    EditText password;

    Button signin;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        email = findViewById(R.id.email_input);
        password = findViewById(R.id.password_input);

        signin = findViewById(R.id.sign_in);
        fAuth = FirebaseAuth.getInstance();

        Typeface mont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.otf");
        email.setTypeface(mont);
        password.setTypeface(mont);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAdd = email.getText().toString();
                String pass = password.getText().toString();


                /*Matches the user credentials to that stored in database*/
                fAuth.signInWithEmailAndPassword(emailAdd, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginScreen.this,"Login Successful", Toast.LENGTH_SHORT).show();


                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Authorized IFAs").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                                    if (doc.getId().equals(fAuth.getUid())) {
                                                        startActivity(new Intent(
                                                                getApplicationContext(),
                                                                NavView.class));
                                                    }
                                                }
                                                launchClient();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginScreen.this,"Error!!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void launchClient() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference client = db.collection("Client List").document((String) fAuth.getUid());
        Log.d("LOGIN", fAuth.getUid());
        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Intent intent = new Intent(getApplicationContext(),
                                ClientBasic.class);
                        String ifa = (String) doc.get("Associated IFA");
                        String first = (String) doc.get("First Name");
                        String last = (String) doc.get("Last Name");
                        intent.putExtra("IFA", ifa);
                        intent.putExtra("First", first);
                        intent.putExtra("Last", last);
                        Log.d("LOGIN", "Starting client activity");
                        startActivity(intent);
                    }
                    else {
                        Log.d("LOGIN", "Doc doesn't exist");
                    }
                }
                else {

                    Log.d("LOGIN", "Couldn't get documents");
                }
            }
        });
    }
}