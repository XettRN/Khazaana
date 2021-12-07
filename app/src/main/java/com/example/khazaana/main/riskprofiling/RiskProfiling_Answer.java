package com.example.khazaana.main.riskprofiling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.khazaana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RiskProfiling_Answer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_profiling_answer);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference clients = db.collection("Client List");
        DocumentReference client = clients.document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String ifa_uid = Objects.requireNonNull(document.get("Associated IFA")).toString();
                        CollectionReference ifas = db.collection("Authorized IFAs");
                        DocumentReference ifa = ifas.document(ifa_uid);
                        CollectionReference clients = ifa.collection("Clients");
                        DocumentReference current_client = clients.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        current_client.update("Risk Profiling Answers", RiskProfilingValues.returnProfilingArray());
                        current_client.update("Risks total", RiskProfilingValues.returnProfiling());
                    }
                }
            }
        });


    }
}