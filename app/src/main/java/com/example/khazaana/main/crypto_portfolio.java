package com.example.khazaana.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khazaana.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class crypto_portfolio extends Fragment {

    List<Map> t = null;
    PieChart pieChart = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crypto_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.name);
        TextView crypto1 = view.findViewById(R.id.crypto1);
        TextView boughtPrice1 = view.findViewById(R.id.cboughtPrice1);
        TextView cryptoOwned1 = view.findViewById(R.id.cryptoOwned1);
        TextView crypto2 = view.findViewById(R.id.crypto2);
        TextView boughtPrice2 = view.findViewById(R.id.cboughtPrice2);
        TextView cryptoOwned2 = view.findViewById(R.id.cryptoOwned2);
        TextView crypto3 = view.findViewById(R.id.crypto3);
        TextView boughtPrice3 = view.findViewById(R.id.cboughtPrice3);
        TextView cryptoOwned3 = view.findViewById(R.id.cryptoOwned3);

        pieChart = view.findViewById(R.id.pieChart2);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.getLegend().setEnabled(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document("A5WkIbLiaub1V1bQ9CRwzLdXBSo2");
        CollectionReference clients = ifa.collection("Clients");
        DocumentReference client = clients.document("24pLjJbK43clJtggGDLPk9ALQfZ2");

        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        String firstName = (String) document.get("First Name");
                        String lastName = (String) document.get("Last Name");
                        textView.setText(" " +firstName + " " + lastName);


                        t = (List<Map>) document.get("Crypto");
                        crypto1.setText("Crypto: " + t.get(0).get("stock"));
                        boughtPrice1.setText("Bought Price: " + t.get(0).get("price"));
                        cryptoOwned1.setText("Crypto Owned: " + t.get(0).get("quantity"));

                        crypto2.setText("Crypto: " + t.get(1).get("stock"));
                        boughtPrice2.setText("Bought Price: " + t.get(1).get("price"));
                        cryptoOwned2.setText("Crypto Owned: " + t.get(1).get("quantity"));

                        crypto3.setText("Crypto: " + t.get(2).get("stock"));
                        boughtPrice3.setText("Bought Price: " + t.get(2).get("price"));
                        cryptoOwned3.setText("Crypto Owned: " + t.get(2).get("quantity"));

                        double total = Double.parseDouble(t.get(0).get("price").toString())*Double.parseDouble(t.get(0).get("quantity").toString()) +
                                Double.parseDouble(t.get(1).get("price").toString())*Double.parseDouble(t.get(1).get("quantity").toString()) +
                                Double.parseDouble(t.get(2).get("price").toString())*Double.parseDouble(t.get(2).get("quantity").toString());

                        double invst1 = (Double.parseDouble(t.get(0).get("price").toString())*Double.parseDouble(t.get(0).get("quantity").toString())) / total;
                        double invst2 = (Double.parseDouble(t.get(1).get("price").toString())*Double.parseDouble(t.get(1).get("quantity").toString())) / total;
                        double invst3 = (Double.parseDouble(t.get(2).get("price").toString())*Double.parseDouble(t.get(2).get("quantity").toString())) / total;
                        List<Number> crypto = new ArrayList<>();
                        crypto.add(invst1);
                        crypto.add(invst2);
                        crypto.add(invst3);
                        pieChart.setData(getPieData(crypto));
                        pieChart.invalidate();

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });



    }

    private PieData getPieData(List<Number> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(list.get(0).floatValue(), ""+t.get(0).get("stock")));
        entries.add(new PieEntry(list.get(1).floatValue(), ""+t.get(1).get("stock")));
        entries.add(new PieEntry(list.get(2).floatValue(), ""+t.get(2).get("stock")));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }

}