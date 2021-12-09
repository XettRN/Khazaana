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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.khazaana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteCrypto extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_crypto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String ifaID = FirebaseAuth.getInstance().getUid();
        String clientID = DeleteStockArgs.fromBundle(getArguments()).getClientID();
        String user = FirebaseAuth.getInstance().getUid();
        assert user != null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cliRef = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients")
                .document(clientID);

        cliRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        List<Map> crypto = (List<Map>) doc.get("Crypto");
                        assert crypto != null;
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < crypto.size(); i++) {
                            list.add(crypto.get(i).get("stock").toString());
                        }

                        AutoCompleteTextView auto = view.findViewById(R.id.deleteCryptoAuto);
                        auto.setAdapter(new ArrayAdapter<>(requireContext(),
                                R.layout.dropdown_item, list));
                    }
                    else {
                        Log.d("DEL_CRYPTO", "Document doesn't exist");
                    }
                }
                else {
                    Log.d("DEL_CRYPTO", "Error getting document: ", task.getException());
                }
            }
        });

        AutoCompleteTextView auto = view.findViewById(R.id.deleteCryptoAuto);
        Button button = view.findViewById(R.id.delete_crypto_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auto.getText().length() <= 0) {
                    Toast.makeText(getContext(), "Please enter currency", Toast.LENGTH_SHORT).show();
                }
                cliRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                List<Map> crypto = (List<Map>) doc.get("Crypto");
                                assert crypto != null;
                                boolean found = false;
                                for (int i = 0; i < crypto.size(); i++) {
                                    if (auto.getText().toString().equals(crypto.get(i).get("stock").toString())) {
                                        cliRef.update("Crypto",
                                                FieldValue.arrayRemove(crypto.get(i)));
                                        NavDirections action = DeleteCryptoDirections
                                                .actionDeleteCryptoToCryptoPortfolio(clientID, ifaID);
                                        Navigation.findNavController(view).navigate(action);
                                    }
                                    found = true;
                                }
                                if (found) {
                                    Toast.makeText(getContext(), "Deleted currency", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "No such currency", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Log.d("DEL_CRYPTO", "Document doesn't exist");
                            }
                        }
                        else {
                            Log.d("DEL_CRYPTO", "Error getting document: ",
                                    task.getException());
                        }
                    }
                });
            }
        });
    }
}