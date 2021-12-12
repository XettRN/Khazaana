package com.example.khazaana.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khazaana.CallAPI;
import com.example.khazaana.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientBasic extends AppCompatActivity {

    double initStock;
    double totalStock;
    double returnStock;

    double initCrypto;
    double totalCrypto;
    double returnCrypto;

    String perf1Name;
    String perf2Name;
    double perf1Price;
    double perf2Price;

    double initAUM;
    double totalAUM;
    double finalReturn;

    TextView aum;
    TextView totalReturn;
    TextView totalBench;
    TextView firstPerf;
    TextView secPerf;
    TextView stockInitAUM;
    TextView stockCurrAUM;
    TextView stockReturnText;
    TextView stockBench;
    TextView cryptoInitAUM;
    TextView cryptoCurrAUM;
    TextView cryptoReturnText;
    TextView cryptoBench;

    List<Number> graph = null;
    List<Map> list = null;
    PieChart pieChart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_basic);



        perf1Name = "";
        perf2Name = "";
        perf1Price = 0;
        perf2Price = 0;
        initStock = 0;
        totalStock = 0;
        initCrypto = 0;
        totalCrypto = 0;
        totalAUM = 0;
        finalReturn = 0;

        //get view elements
        TextView name = findViewById(R.id.clientName);
        TextView stockTitle = findViewById(R.id.stocks);
        TextView cryptoTitle = findViewById(R.id.cryptoTitle);
        aum = findViewById(R.id.aum);
        totalReturn = findViewById(R.id.returnP);
        totalBench = findViewById(R.id.bench_return);
        firstPerf = findViewById(R.id.performer1);
        secPerf = findViewById(R.id.performer2);
        stockInitAUM = findViewById(R.id.initial_aum1);
        stockCurrAUM = findViewById(R.id.current_aum1);
        stockReturnText = findViewById(R.id.stock_return);
        stockBench = findViewById(R.id.stock_return_bench);
        cryptoInitAUM = findViewById(R.id.initial_aum2);
        cryptoCurrAUM = findViewById(R.id.current_aum2);
        cryptoReturnText = findViewById(R.id.crypto_return);
        cryptoBench = findViewById(R.id.crypto_return_bench);
        pieChart = findViewById(R.id.pieChart3);
        graph = new ArrayList<>();

        //fetch client document from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String ifaID = intent.getStringExtra("IFA");
        String first = intent.getStringExtra("First");
        String last = intent.getStringExtra("Last");
        Query client = db.collection("Authorized IFAs")
                .document(ifaID)
                .collection("Clients")
                .whereEqualTo("First Name", first)
                .whereEqualTo("Last Name", last);

        //update layout with data
        client.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                    if (doc.exists()) {
                        //set client name
                        String firstName = (String) doc.get("First Name");
                        String lastName = (String) doc.get("Last Name");
                        name.setText(firstName + " " + lastName);

                        //get client portfolio assets
                        List<Map> stocks = (List<Map>) doc.get("Stocks");
                        assert stocks != null;
                        List<Map> crypto = (List<Map>) doc.get("Crypto");
                        assert crypto != null;
                        double totalAssets = stocks.size() + crypto.size();

                        CallAPI callAPI = new CallAPI();

                        //calculations and api calls
                        if (stocks.size() > 0) {
                            for (int i = 0; i < stocks.size(); i++) {
                                AssetEntry a = new AssetEntry();
                                a.setStock(stocks.get(i).get("stock").toString());
                                a.setPrice(Float.parseFloat(stocks.get(i).get("price").toString()));
                                a.setQuantity(Float.parseFloat(stocks.get(i).get("quantity").toString()));

                                initStock += a.getPrice();
                                initAUM += a.getPrice();

                                callAPI.calcStock(getApplicationContext(), a, new CallAPI.StockListener() {
                                    @Override
                                    public void OnError(String message) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnResponse(String name, double stockPrice, double stockReturn) {
                                        if (stockPrice > perf1Price) {
                                            perf1Price = stockPrice;
                                            firstPerf.setText(name);
                                        }
                                        else if (stockPrice > perf2Price) {
                                            perf2Price = stockPrice;
                                            secPerf.setText(name);
                                        }

                                        totalStock += (stockPrice * a.getQuantity());
                                        totalAUM += (stockPrice * a.getQuantity());
                                        returnStock += (stockReturn * a.getQuantity());
                                        finalReturn += (stockReturn * a.getQuantity());

                                        stockInitAUM.setText("Initial AUM: " + initStock);
                                        stockCurrAUM.setText("Current AUM: " + totalStock);
                                        stockReturnText.setText("Return: " + returnStock);
                                        stockBench.setText("Benchmark Return: " + (0.1 * initStock));

                                        aum.setText("AUM " + totalAUM);
                                        totalReturn.setText("Return: " + finalReturn);
                                        totalBench.setText("Benchmark Return: " + (0.1 * initAUM));
                                        graph.add((totalStock/totalAUM)*100);
                                        Log.d("Pie chart", "Pie chart data: "+graph);
                                        pieChart.setData(getPieData(graph));
                                        pieChart.invalidate();
                                    }
                                });
                            }
                        }
                        else {
                            stockInitAUM.setText(stockInitAUM.getText() + " 0");
                            stockCurrAUM.setText(stockCurrAUM.getText() + " 0");
                            stockReturnText.setText(stockReturnText.getText() + " 0");
                            stockBench.setText(stockBench.getText() + " 0");
                        }
                        if (crypto.size() > 0) {
                            for (int i = 0; i < crypto.size(); i++) {
                                AssetEntry a = new AssetEntry();
                                a.setStock(crypto.get(i).get("stock").toString());
                                a.setPrice(Float.parseFloat(stocks.get(i).get("price").toString()));
                                a.setQuantity(Float.parseFloat(stocks.get(i).get("quantity").toString()));

                                initCrypto += a.getPrice();
                                initAUM += a.getPrice();

                                callAPI.calcCrypto(getApplicationContext(), a, new CallAPI.CryptoListener() {
                                    @Override
                                    public void OnError(String message) {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnResponse(String name, double cryptoPrice, double cryptoReturn, Double[] prices) {
                                        if (cryptoPrice > perf1Price) {
                                            perf1Price = cryptoPrice;
                                            firstPerf.setText(name);
                                        }
                                        else if (cryptoPrice > perf2Price) {
                                            perf2Price = cryptoPrice;
                                            secPerf.setText(name);
                                        }

                                        totalCrypto += (cryptoPrice * a.getQuantity());
                                        totalAUM += (cryptoPrice * a.getQuantity());
                                        returnCrypto += (cryptoReturn * a.getQuantity());
                                        finalReturn += (cryptoReturn * a.getQuantity());

                                        cryptoInitAUM.setText("Initial AUM: " + initCrypto);
                                        cryptoCurrAUM.setText("Current AUM: " + totalCrypto);
                                        cryptoReturnText.setText("Return: " + returnCrypto);
                                        cryptoBench.setText("Benchmark Return: " + (0.1 * initCrypto));

                                        aum.setText("AUM " + totalAUM);
                                        totalReturn.setText("Return: " + finalReturn);
                                        totalBench.setText("Benchmark Return: " + (0.1 * initAUM));
                                    }
                                });
                            }
                        }
                        else {
                            cryptoInitAUM.setText(cryptoInitAUM.getText() + " 0");
                            cryptoCurrAUM.setText(cryptoCurrAUM.getText() + " 0");
                            cryptoReturnText.setText(cryptoReturnText.getText() + " 0");
                            cryptoBench.setText(cryptoBench.getText() + " 0");
                        }
                        if (stocks.size() == 0 && crypto.size() == 0) {
                            aum.setText("AUM 0");
                            totalReturn.setText("Return: 0");
                            totalBench.setText("Benchmark Return: 0");
                        }
                    }
                    else {
                        Log.d("IND_PORT", "Document doesn't exist");
                    }
                }
                else {
                    Log.d("IND_PORT", "Get failed with", task.getException());
                }
            }
        });
    }

    private PieData getPieData(List<Number> l) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("Data", "data: "+list);
        if (l.size() == 1) {
            entries.add(new PieEntry(l.get(0).floatValue(), "Stocks"));
        } else {
            entries.add(new PieEntry(l.get(0).floatValue(), "Stocks"));
            entries.add(new PieEntry(l.get(1).floatValue(), "Crypto"));
        }


        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }

}