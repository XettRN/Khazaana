package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class userDetails extends AppCompatActivity {

    Button next, sign_out;
    TextView fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        next = findViewById(R.id.next2);
        sign_out = findViewById(R.id.sign_out);
        fName = findViewById(R.id.first);
        lName = findViewById(R.id.last);

        String userID = FirebaseAuth.getInstance().getUid();

        DocumentReference d = FirebaseFirestore.getInstance().collection("Client List").document(userID);

        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                        fName.setText(task.getResult().getData().get("First Name").toString());
                        lName.setText(task.getResult().getData().get("Last Name").toString());
                    }
                else {
                        DocumentReference d = FirebaseFirestore.getInstance().collection("Authorized IFAs").document(userID);
                        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().exists()) {
                                    fName.setText(task.getResult().getData().get("First Name").toString());
                                    lName.setText(task.getResult().getData().get("Last Name").toString());
                                }
                            }
                        });
                    }

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