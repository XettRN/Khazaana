package com.example.khazaana.main;

import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class individualClientPortfolio extends Fragment {

    PieChart pieChart = null;
    TextView perform1 = null;
    TextView perform2 = null;
    TextView current_aum1 = null;
    TextView return1 = null;
    TextView current_aum2 = null;
    TextView return2 = null;
    String stock1, stock2, stock3 = null;
    double price1, price2, price3 = 0;
    double stocks_total = 0;
    double crypto_total = 0;
    List<Map> stocks = null;
    List<Map> crypto = null;
    TextView initial_aum1 = null;
    TextView initial_aum2 = null;
    List<Number> equity = null;
    double total1, total2;
    TextView aum = null;
    TextView returnP = null;
    DecimalFormat d = new DecimalFormat("#.####");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual_client_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name = view.findViewById(R.id.clientName);
        pieChart = view.findViewById(R.id.pieChart3);
        aum = view.findViewById(R.id.aum);
        returnP = view.findViewById(R.id.returnP);
        TextView bench_return = view.findViewById(R.id.bench_return);
        perform1 = view.findViewById(R.id.performer1);
        perform2 = view.findViewById(R.id.performer2);
        initial_aum1 = view.findViewById(R.id.initial_aum1);
        current_aum1 = view.findViewById(R.id.current_aum1);
        return1 = view.findViewById(R.id.equity_return);
        TextView bench_return1 = view.findViewById(R.id.equity_return_bench);
        initial_aum2 = view.findViewById(R.id.initial_aum2);
        current_aum2 = view.findViewById(R.id.current_aum2);
        return2 = view.findViewById(R.id.crypto_return);
        TextView bench_return2 = view.findViewById(R.id.crypto_return_bench);
        TextView stockTitle = view.findViewById(R.id.stocks);
        TextView cryptoTitle = view.findViewById(R.id.crypto);


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
                        name.setText(" " + firstName + " " + lastName);

                        stocks = (List<Map>) document.get("Stocks");
                        assert stocks != null;
                        double totalS = 0;
                        for (int i = 0; i < 3; i++) {
                            totalS = totalS + Double.parseDouble(stocks.get(i).get("price").toString()) * Double.parseDouble(stocks.get(i).get("quantity").toString());
                        }
                        initial_aum1.setText(initial_aum1.getText() + " " + totalS);
                        total1 = totalS;
                        bench_return1.setText(bench_return1.getText() + " 21%");

                        crypto = (List<Map>) document.get("Crypto");
                        assert crypto != null;
                        double totalC = 0;
                        for (int i = 0; i < 3; i++) {
                            totalC = totalC + Double.parseDouble(crypto.get(i).get("price").toString()) * Double.parseDouble(crypto.get(i).get("quantity").toString());
                        }
                        crypto_total = totalC;
                        initial_aum2.setText(initial_aum2.getText() + " " + totalC);
                        total2 = totalC;
                        return2.setText(return2.getText() + " 16%");
                        bench_return2.setText(bench_return2.getText() + " 12%");

                        bench_return.setText(bench_return.getText() + " 10%");

                        equity = (List<Number>) document.get("Equity");

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        new stockPriceTask1().execute("https://finnhub-backend.herokuapp.com/price?symbol=AAPL");
        new stockPriceTask2().execute("https://finnhub-backend.herokuapp.com/price?symbol=TSLA");
        new stockPriceTask3().execute("https://finnhub-backend.herokuapp.com/price?symbol=AMZN");

        View root = view;
        stockTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = individualClientPortfolioDirections
                        .actionIndividualClientPortfolioToStockPortfolio();
                Navigation.findNavController(root).navigate(navDirections);
            }
        });

        cryptoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = individualClientPortfolioDirections.actionIndividualClientPortfolioToCryptoPortfolio();
                Navigation.findNavController(root).navigate(navDirections);
            }
        });
    }

    private class stockPriceTask1 extends AsyncTask<String, String, String> {
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
                                stocks_total = stocks_total + Double.parseDouble(j.get("current price").toString());
                                stock1 = "APPL";
                                price1 = Double.parseDouble(j.get("current price").toString());
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

    private class stockPriceTask2 extends AsyncTask<String, String, String> {
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
                                stocks_total = stocks_total + Double.parseDouble(j.get("current price").toString());
                                stock2 = "TSLA";
                                price2 = Double.parseDouble(j.get("current price").toString());
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

    private class stockPriceTask3 extends AsyncTask<String, String, String> {
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
                                stocks_total = stocks_total + Double.parseDouble(j.get("current price").toString());
                                current_aum1.setText(current_aum1.getText()+""+stocks_total);
                                double stocks_return = ((stocks_total - total1)/ total1)*100;
                                return1.setText(return1.getText() + "" + stocks_return);
                                stock3 = "AMZN";
                                price3 = Double.parseDouble(j.get("current price").toString());
                                current_aum2.setText(current_aum2.getText() + ""+crypto_total);

                                if (price1 > price2 && price1 > price3) {
                                    perform1.setText(stock1 + ":  "+price1);
                                    if (price2 > price3) {
                                        perform2.setText(stock2 + ":  "+price2);
                                    } else {
                                        perform2.setText(stock3 + ":  "+price3);
                                    }
                                } else if (price2 > price1 && price2 > price3) {
                                    perform1.setText(stock2 + ":  "+price2);
                                    if (price1 > price3) {
                                        perform2.setText(stock1 + ":  "+price1);
                                    } else {
                                        perform2.setText(stock3 + ":  "+price3);
                                    }
                                } else {
                                    perform1.setText(stock3 + ":  "+price3);
                                    if (price1 > price3) {
                                        perform2.setText(stock1 + ":  "+price1);
                                    } else {
                                        perform2.setText(stock2 + ":  "+price2);
                                    }
                                }

                                double total = stocks_total + crypto_total;
                                aum.setText(aum.getText() + " "+total);
                                double returnValue = ((total - (total1+total2))/(total1 + total2))*100;
                                returnP.setText(returnP.getText() + " "+d.format(returnValue));
                                List<Number> a = new ArrayList<>();
                                a.add((stocks_total/total)*100);
                                a.add((crypto_total/total)*100);
                                pieChart.setData(getPieData(a));
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
        entries.add(new PieEntry(list.get(0).floatValue(), "Stocks"));
        entries.add(new PieEntry(list.get(1).floatValue(), "Crypto"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }
}