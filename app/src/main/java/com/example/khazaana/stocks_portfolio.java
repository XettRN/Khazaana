package com.example.khazaana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class stocks_portfolio extends AppCompatActivity {

    List<Map> t = null;
    TextView currentP1 = null;
    TextView currentP2 = null;
    TextView currentP3 = null;
    TextView return1 = null;
    TextView return2 = null;
    TextView return3 = null;
    double buyingPrice1 = 0;
    double buyingPrice2 = 0;
    double cp1 = 0;
    double cp2 = 0;
    double q1 = 0;
    double q2 = 0;
    List<Number> stocks = null;
    PieChart pieChart = null;
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
        Button next = findViewById(R.id.nextScreen);
        currentP1 = findViewById(R.id.currentPrice1);
        currentP2 = findViewById(R.id.currentPrice2);
        currentP3 = findViewById(R.id.currentPrice3);
        return1 = findViewById(R.id.return1);
        return2 = findViewById(R.id.return2);
        return3 = findViewById(R.id.return3);

        pieChart = findViewById(R.id.pieChart1);
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

                        t = (List<Map>) document.get("Stocks");
                        assert t != null;
                        stock1.setText("Stock: " + t.get(0).get("stock"));
                        boughtPrice1.setText("Bought Price: " + t.get(0).get("price"));
                        sharesOwned1.setText("Shares Owned: " + t.get(0).get("quantity"));

                        buyingPrice1 = (Double) t.get(0).get("price");
                        q1 = (Double) t.get(0).get("quantity");

                        stock2.setText("Stock: " + t.get(1).get("stock"));
                        boughtPrice2.setText("Bought Price: " + t.get(1).get("price"));
                        sharesOwned2.setText("Shares Owned: " + t.get(1).get("quantity"));

                        buyingPrice2 = (Double) t.get(1).get("price");
                        q2 = (Double) t.get(1).get("quantity");


                        /*double total = (Double)t.get(0).get("price")*(Double)t.get(0).get("quantity") +
                                (Double)t.get(1).get("price")*(Double)t.get(1).get("quantity");
                        double invst1 = ((Double)t.get(0).get("price")*(Double)t.get(0).get("quantity")) / total;
                        double invst2 = ((Double)t.get(1).get("price")*(Double)t.get(1).get("quantity")) / total;

                        stocks = new ArrayList<>();
                        stocks.add(invst1);
                        stocks.add(invst2);
                        pieChart.setData(getPieData(stocks));
                        pieChart.invalidate();*/
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        stock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), specific_equity.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), crypto_portfolio.class));
            }
        });

        new priceTask1().execute("https://finnhub-backend.herokuapp.com/price?symbol=AAPL");
        new priceTask2().execute("https://finnhub-backend.herokuapp.com/price?symbol=TSLA");


    }

    private class priceTask1 extends AsyncTask<String, String, String> {
        String data = "";

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                while (line != null) {
                    line = reader.readLine();
                    data = data + line;
                }
                try {
                    JSONObject j = new JSONObject(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP1.setText("Current Price: $" + j.get("current price"));
                                cp1 = (Double) j.get("current price");
                                double returnS1 = ((cp1 - buyingPrice1)/buyingPrice1)*100;
                                return1.setText("Return: "+returnS1+"%");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class priceTask2 extends AsyncTask<String, String, String> {
        String data = "";

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                while (line != null) {
                    line = reader.readLine();
                    data = data + line;
                }
                try {
                    JSONObject j = new JSONObject(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP2.setText("Current Price: $" + j.get("current price"));
                                cp2 = (Double) j.get("current price");
                                double returnS2 = ((cp2 - buyingPrice2)/buyingPrice2)*100;
                                return2.setText("Return: "+returnS2+"%");


                                double total = cp1*q1 + cp2*q2;
                                double invst1 = cp1*q1 / total;
                                double invst2 = cp2*q2 / total;

                                stocks = new ArrayList<>();
                                stocks.add(invst1);
                                stocks.add(invst2);
                                pieChart.setData(getPieData(stocks));
                                pieChart.invalidate();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private PieData getPieData(List<Number> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(list.get(0).floatValue(), ""+t.get(0).get("stock")));
        entries.add(new PieEntry(list.get(1).floatValue(), ""+t.get(1).get("stock")));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }
}