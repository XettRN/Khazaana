package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class stocks_portfolio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_portfolio);

        TextView textView = findViewById(R.id.name);
        TextView stock1 = findViewById(R.id.stock1);
        TextView boughtPrice1 = findViewById(R.id.boughtPrice1);
        TextView sharesOwned1 = findViewById(R.id.sharesOwned1);
        TextView stock2 = findViewById(R.id.stock2);
        TextView boughtPrice2 = findViewById(R.id.boughtPrice2);
        TextView sharesOwned2 = findViewById(R.id.sharesOwned2);

        PieChart pieChart = findViewById(R.id.pieChart);
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

                        List<Number> equity = (List<Number>) document.get("Equity");
                        pieChart.setData(getPieData(equity));
                        pieChart.invalidate();
                        List<Map> t = (List<Map>) document.get("Stocks");
                        stock1.setText("Stock: " + t.get(0).get("stock"));
                        boughtPrice1.setText("Bought Price: " + t.get(0).get("price"));
                        sharesOwned1.setText("Shares Owned: " + t.get(0).get("quantity"));
                        stock2.setText("Stock: " + t.get(1).get("stock"));
                        boughtPrice2.setText("Bought Price: " + t.get(1).get("price"));
                        sharesOwned2.setText("Shares Owned: " + t.get(1).get("quantity"));
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
        entries.add(new PieEntry(list.get(0).floatValue(), "Stocks"));
        entries.add(new PieEntry(list.get(1).floatValue(), "Crypto"));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }
}