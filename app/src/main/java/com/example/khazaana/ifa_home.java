package com.example.khazaana;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ifa_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ifa_home extends Fragment {

    public static final String USER_ID = "com.example.khazaana.USER";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ifa_home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ifa_home.
     */
    // TODO: Rename and change types and number of parameters
    public static ifa_home newInstance(String param1, String param2) {
        ifa_home fragment = new ifa_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ifa_home, container, false);

        TextView name = view.findViewById(R.id.heading3);


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

        Button clients = view.findViewById(R.id.button2);
        Button equity = view.findViewById(R.id.button);

        clients.setOnClickListener(view1 -> {
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.main_container, new client_list());
            trans.addToBackStack(null);
            trans.commit();
        });

        equity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Portfolio.class);
                startActivity(intent);
            }
        });


        return view;
    }
}