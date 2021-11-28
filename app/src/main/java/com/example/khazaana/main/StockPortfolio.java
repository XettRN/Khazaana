package com.example.khazaana.main;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.khazaana.R;
import com.example.khazaana.crypto_portfolio;
import com.example.khazaana.specific_equity;
import com.example.khazaana.stocks_portfolio;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockPortfolio extends Fragment {
    List<Map> t = null;

    TextView currentP1 = null;
    TextView currentP2 = null;
    TextView currentP3 = null;
    TextView return1 = null;
    TextView return2 = null;
    TextView return3 = null;
    double buyingPrice1 = 0;
    double buyingPrice2 = 0;
    double buyingPrice3 = 0;
    double cp1 = 0;
    double cp2 = 0;
    double cp3 = 0;
    double q1 = 0;
    double q2 = 0;
    double q3 = 0;
    List<Number> stocks = null;
    PieChart pieChart = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //changed all findViewById to view.findViewById
        TextView textView = view.findViewById(R.id.name);

        TextView stock1 = view.findViewById(R.id.stock1);
        TextView boughtPrice1 = view.findViewById(R.id.boughtPrice1);
        TextView sharesOwned1 = view.findViewById(R.id.sharesOwned1);

        TextView stock2 = view.findViewById(R.id.stock2);
        TextView boughtPrice2 = view.findViewById(R.id.boughtPrice2);
        TextView sharesOwned2 = view.findViewById(R.id.sharesOwned2);

        TextView stock3 = view.findViewById(R.id.stock3);
        TextView boughtPrice3 = view.findViewById(R.id.boughtPrice3);
        TextView sharesOwned3 = view.findViewById(R.id.sharesOwned3);

        Button next = view.findViewById(R.id.nextScreen);

        currentP1 = view.findViewById(R.id.currentPrice1);
        currentP2 = view.findViewById(R.id.currentPrice2);
        currentP3 = view.findViewById(R.id.currentPrice3);

        return1 = view.findViewById(R.id.return1);
        return2 = view.findViewById(R.id.return2);
        return3 = view.findViewById(R.id.return3);

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

                        t = (List<Map>) document.get("Stocks");
                        assert t != null;

                        stock1.setText("Stock: " + t.get(0).get("stock"));
                        boughtPrice1.setText("Bought Price: $" + t.get(0).get("price"));
                        sharesOwned1.setText("Shares Owned: " + t.get(0).get("quantity"));
                        buyingPrice1 = Double.parseDouble(t.get(0).get("price").toString());
                        q1 = Double.parseDouble(t.get(0).get("quantity").toString());

                        stock2.setText("Stock: " + t.get(1).get("stock"));
                        boughtPrice2.setText("Bought Price: $" + t.get(1).get("price"));
                        sharesOwned2.setText("Shares Owned: " + t.get(1).get("quantity"));
                        buyingPrice2 = Double.parseDouble(t.get(1).get("price").toString());
                        q2 = Double.parseDouble(t.get(1).get("quantity").toString());

                        stock3.setText("Stock: " + t.get(2).get("stock"));
                        boughtPrice3.setText("Bought Price: $" + t.get(2).get("price"));
                        sharesOwned3.setText("Shares Owned: " + t.get(2).get("quantity"));
                        buyingPrice3 = Double.parseDouble(t.get(2).get("price").toString());
                        q3 = Double.parseDouble(t.get(2).get("quantity").toString());


                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        View root = view;
        stock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = StockPortfolioDirections.actionStockPortfolioToSpecificStock();
                Navigation.findNavController(root).navigate(navDirections);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections navDirections = StockPortfolioDirections.actionStockPortfolioToCryptoPortfolio();
                Navigation.findNavController(root).navigate(navDirections);
            }
        });

        Toolbar stockBar = view.findViewById(R.id.stockBar);
        stockBar.inflateMenu(R.menu.asset_toolbar);
        stockBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_stock_button) {
                    NavDirections navDirections = StockPortfolioDirections.actionStockPortfolioToAddStockFrag();
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                return StockPortfolio.super.onOptionsItemSelected(item);
            }
        });

        new StockPortfolio.priceTask1().execute("https://finnhub-backend.herokuapp.com/price?symbol=AAPL");
        new StockPortfolio.priceTask2().execute("https://finnhub-backend.herokuapp.com/price?symbol=TSLA");
        new StockPortfolio.priceTask3().execute("https://finnhub-backend.herokuapp.com/price?symbol=AMZN");
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
                    getActivity().runOnUiThread(new Runnable() { //add getActivity. before this method
                        @Override
                        public void run() {
                            try {
                                currentP1.setText("Current Price: $" + j.get("current price"));
                                cp1 = Double.parseDouble(j.get("current price").toString());
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP2.setText("Current Price: $" + j.get("current price"));
                                cp2 = Double.parseDouble(j.get("current price").toString());
                                double returnS2 = ((cp2 - buyingPrice2)/buyingPrice2)*100;
                                return2.setText("Return: "+returnS2+"%");

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

    private class priceTask3 extends AsyncTask<String, String, String> {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP3.setText("Current Price: $" + j.get("current price"));
                                cp3 = Double.parseDouble(j.get("current price").toString());
                                double returnS3 = ((cp3 - buyingPrice3)/buyingPrice3)*100;
                                return3.setText("Return: "+returnS3+"%");


                                double total = cp1*q1 + cp2*q2 + cp3*q3;
                                double invst1 = cp1*q1 / total;
                                double invst2 = cp2*q2 / total;
                                double invst3 = cp3*q3 / total;

                                stocks = new ArrayList<>();
                                stocks.add(invst1);
                                stocks.add(invst2);
                                stocks.add(invst3);
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
        entries.add(new PieEntry(list.get(2).floatValue(), ""+t.get(2).get("stock")));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }
}