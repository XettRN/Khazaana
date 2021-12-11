package com.example.khazaana.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khazaana.R;
import com.example.khazaana.main.client.ClientRiskProfilingMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class Profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ifa_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView fName = view.findViewById(R.id.first_name_input);
        TextView lName = view.findViewById(R.id.last_name_input);
        TextView email = view.findViewById(R.id.email_input);

        String userID = FirebaseAuth.getInstance().getUid();

        DocumentReference d = FirebaseFirestore.getInstance().collection("Client List").document(userID);

        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    fName.setText(task.getResult().getData().get("First Name").toString());
                    lName.setText(task.getResult().getData().get("Last Name").toString());
                    email.setText(task.getResult().getData().get("Email").toString());
                }
                else {
                    DocumentReference d = FirebaseFirestore.getInstance().collection("Authorized IFAs").document(userID);
                    d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                fName.setText(task.getResult().getData().get("First Name").toString());
                                lName.setText(task.getResult().getData().get("Last Name").toString());
                                email.setText(task.getResult().getData().get("Email Address").toString());
                            }
                        }
                    });
                }

            }
        });

        Button previousPage = view.findViewById(R.id.back_ifa_profile);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = ProfileDirections.actionProfileFragToIFAAccountSettings();
                Navigation.findNavController(view).navigate(action);
            }
        });
        Button risk_profiling = view.findViewById(R.id.risks2);
        risk_profiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ClientRiskProfilingMain.class);
                startActivity(intent);
            }
        });
    }
}