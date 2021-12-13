package com.example.khazaana.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khazaana.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClient extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String user = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference clients = db.collection("Authorized IFAs")
                .document(user).collection("Clients");

        EditText fieldFirst = view.findViewById(R.id.field_first_name);
        EditText fieldLast = view.findViewById(R.id.field_last_name);

        Button addClient = view.findViewById(R.id.submit_new_client);
        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = fieldFirst.getText().toString();
                String last = fieldLast.getText().toString();
                if (first.length() <= 0 || last.length() <= 0) {
                    Toast.makeText(getContext(), "Please input name", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> data = new HashMap<>();
                    ArrayList<AssetEntry> stocks = new ArrayList<>();
                    ArrayList<AssetEntry> crypto = new ArrayList<>();
                    ArrayList<Number> equity = new ArrayList<>();
                    equity.add(50);
                    equity.add(50);
                    data.put("First Name", first);
                    data.put("Last Name", last);
                    data.put("Stocks", stocks);
                    data.put("Crypto", crypto);
                    data.put("Equity", equity);

                    clients.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("ADD_CLIENT", "Added new client");
                            NavDirections action = AddClientDirections.actionAddClientFragToClientsFrag();
                            Navigation.findNavController(view).navigate(action);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("ADD_CLIENT", "Failure to add new client");
                        }
                    });
                }
            }
        });
    }
}