package com.example.khazaana.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.khazaana.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCrypto extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_crypto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String clientID = AddCryptoArgs.fromBundle(getArguments()).getClient();

        String user = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference client = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients")
                .document(clientID);

        AutoCompleteTextView autoText = view.findViewById(R.id.cryptoAuto);
        TextInputEditText amountText = view.findViewById(R.id.field_crypto_amount);

        String[] currencies = getResources().getStringArray(R.array.cryptocurrencies);
        autoText.setAdapter(new ArrayAdapter<String>(requireContext(),
                R.layout.dropdown_item, currencies));

        Button addCrypto = view.findViewById(R.id.add_crypto_button);
        addCrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoText.getText().length() <= 0 || amountText.getText().length() <= 0) {
                    Toast.makeText(getContext(), "Please input crypto details", Toast.LENGTH_SHORT).show();
                }
                else {
                    String auto = autoText.getText().toString();
                    float amount = Float.parseFloat(amountText.getText().toString());

                    CryptoEntry entry = new CryptoEntry(auto, amount);
                    client.update("Crypto", FieldValue.arrayUnion(entry));

                    NavDirections action = AddCryptoDirections.actionAddCryptoToCryptoPortfolio();
                    Navigation.findNavController(view).navigate(action);
                }
            }
        });
    }
}