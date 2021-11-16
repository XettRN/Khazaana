package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IFAHome extends AppCompatActivity {

    public static final String USER_ID = "com.example.khazaana.USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ifa_home);

        TextView name = findViewById(R.id.heading3);

        String user = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        assert user != null;
        DocumentReference ifa = ifas.document(user);
        ifa.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        String first = (String) doc.get("First Name");
                        name.setText("Welcome, " + first);
                    }
                    else {
                        Log.d("HOME", "No such document");
                    }
                }
                else {
                    Log.d("HOME", "get failed with ", task.getException());
                }
            }
        });


    }

    public void goClientList(View view) {
        Intent intent = new Intent(this, ClientList.class);
        String user = FirebaseAuth.getInstance().getUid();
        intent.putExtra(USER_ID, user);
        startActivity(intent);
    }

    public void goPortfolio(View view) {
        Intent intent = new Intent(this, Portfolio.class);
        startActivity(intent);
    }
}