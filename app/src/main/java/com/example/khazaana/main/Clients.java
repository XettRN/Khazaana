package com.example.khazaana.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.khazaana.AddData;
import com.example.khazaana.Portfolio;
import com.example.khazaana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Clients extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar clientBar = view.findViewById(R.id.clientBar);
        clientBar.inflateMenu(R.menu.client_toolbar);
        clientBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_client_button) {
                    NavDirections action = ClientsDirections.actionClientsFragToAddClientFrag();
                    Navigation.findNavController(view).navigate(action);
                    return true;
                }
                return Clients.super.onOptionsItemSelected(item);
            }
        });

        LinearLayout layout = view.findViewById(R.id.client_list);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //String[] clients = getResources().getStringArray(R.array.clients);

        String user = FirebaseAuth.getInstance().getUid();
        assert user != null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cliRef = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients");
        cliRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("CLIENTS", document.getId() + " => " + document.getData());
                        String fullName = document.get("First Name") + " " + document.get("Last Name");
                        addClientToList(view, fullName, params, layout, "EJCyh9saTPZ9YzHvdtdN");
                    }
                }
                else {
                    Log.d("CLIENTS", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    private void addClientToList(View view, String client,
                                 ViewGroup.LayoutParams params, LinearLayout layout,
                                 String clientID) {
        View root = view;
        TextView textView = new TextView(getContext());
        textView.setText(client);
        textView.setLayoutParams(params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = ClientsDirections
                        .actionClientsFragToIndividualClientPortfolio(clientID);
                Navigation.findNavController(root).navigate(navDirections);
            }
        });
        layout.addView(textView);
    }
}