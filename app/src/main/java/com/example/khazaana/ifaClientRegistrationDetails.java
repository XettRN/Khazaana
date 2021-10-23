package com.example.khazaana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ifaClientRegistrationDetails extends AppCompatActivity {

    Button next, sign_out;
    TextView fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifa_client_registration_details);
        next = findViewById(R.id.next2);
        sign_out = findViewById(R.id.sign_out);
        fName = findViewById(R.id.first);
        lName = findViewById(R.id.last);

        String userID = FirebaseAuth.getInstance().getUid();
        DocumentReference dref = FirebaseFirestore.getInstance().collection("Authorized IFAs").document(userID);

        dref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fName.setText(value.getString("First Name"));
                lName.setText(value.getString("Last Name"));
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoadScreen.class));
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}