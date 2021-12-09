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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khazaana.CallAPI;
import com.example.khazaana.R;
import com.example.khazaana.RequestSingleton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class individualClientPortfolio extends Fragment {

    /*
    PieChart pieChart = null;
    TextView perform1 = null;
    TextView perform2 = null;
    TextView current_aum1 = null;
    TextView return1 = null;
    TextView current_aum2 = null;
    TextView return2 = null;
    String stock1, stock2, stock3 = null;
    double price1, price2, price3 = 0;

    String crypto1, crypto2, crypto3 = null;
    double cprice1, cprice2, cprice3 = 0;

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
    List<Number> a = null;
    */
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

    int counter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual_client_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        TextView name = view.findViewById(R.id.clientName);
        TextView stockTitle = view.findViewById(R.id.stocks);
        TextView cryptoTitle = view.findViewById(R.id.cryptoTitle);
        aum = view.findViewById(R.id.aum);
        totalReturn = view.findViewById(R.id.returnP);
        totalBench = view.findViewById(R.id.bench_return);
        firstPerf = view.findViewById(R.id.performer1);
        secPerf = view.findViewById(R.id.performer2);
        stockInitAUM = view.findViewById(R.id.initial_aum1);
        stockCurrAUM = view.findViewById(R.id.current_aum1);
        stockReturnText = view.findViewById(R.id.stock_return);
        stockBench = view.findViewById(R.id.stock_return_bench);
        cryptoInitAUM = view.findViewById(R.id.initial_aum2);
        cryptoCurrAUM = view.findViewById(R.id.current_aum2);
        cryptoReturnText = view.findViewById(R.id.crypto_return);
        cryptoBench = view.findViewById(R.id.crypto_return_bench);

        //fetch client document from database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String ifaID = individualClientPortfolioArgs.fromBundle(getArguments()).getIfaID();
        String clientID = individualClientPortfolioArgs.fromBundle(getArguments()).getClientID();
        DocumentReference client = db.collection("Authorized IFAs")
                .document(ifaID)
                .collection("Clients")
                .document(clientID);

        //update layout with data
        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
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
                        counter = 0;

                        //calculations and api calls
                        if (stocks.size() > 0) {
                            for (int i = 0; i < stocks.size(); i++) {
                                AssetEntry a = new AssetEntry();
                                a.setStock(stocks.get(i).get("stock").toString());
                                a.setPrice(Float.parseFloat(stocks.get(i).get("price").toString()));
                                a.setQuantity(Float.parseFloat(stocks.get(i).get("quantity").toString()));

                                initStock += a.getPrice();
                                initAUM += a.getPrice();

                                callAPI.calcStock(getContext(), a, new CallAPI.StockListener() {
                                    @Override
                                    public void OnError(String message) {
                                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
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
                                        counter++;

                                        stockInitAUM.setText("Initial AUM: " + initStock);
                                        stockCurrAUM.setText("Current AUM: " + totalStock);
                                        stockReturnText.setText("Return: " + returnStock);
                                        stockBench.setText("Benchmark Return: " + (0.1 * initStock));

                                        aum.setText("AUM " + totalAUM);
                                        totalReturn.setText("Return: " + finalReturn);
                                        totalBench.setText("Benchmark Return: " + (0.1 * initAUM));
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
                            //calcCrypto(crypto);
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

        View root = view;
        stockTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = individualClientPortfolioDirections
                        .actionIndividualClientPortfolioToStockPortfolio((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"));
                Navigation.findNavController(root).navigate(navDirections);
            }
        });

        cryptoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = individualClientPortfolioDirections.actionIndividualClientPortfolioToCryptoPortfolio((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"));
                Navigation.findNavController(root).navigate(navDirections);
            }
        });
    }

    private void calcCrypto(List<Map> crypto) {
        String url = "https://finnhub-backend.herokuapp.com/crypto/ticker?symbol=";
        initCrypto = 0;
        totalCrypto = 0;

        for (int i = 0; i < crypto.size() - 1; i++) {
            initCrypto += Double.parseDouble(crypto.get(i).get("price").toString());
            String complete = url + crypto.get(i).get("stock");
            Log.d("IND_PORT", complete);
            final String name = crypto.get(i).get("stock").toString();
            final double quantity = Double.parseDouble(crypto.get(i).get("quantity").toString());
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, complete,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        double cryptoPrice = Double
                                .parseDouble(response.get(response.length() - 1).toString())
                                * quantity;
                        if (cryptoPrice > perf1Price) {
                            perf1Price = cryptoPrice;
                            firstPerf.setText(name);
                        }
                        else if (cryptoPrice > perf2Price) {
                            perf2Price = cryptoPrice;
                            secPerf.setText(name);
                        }
                        totalCrypto += cryptoPrice;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            });
            RequestSingleton.getInstance(getContext()).addToRequestQueue(request);
        }

        //final request, update view
        initCrypto += Double.parseDouble(crypto.get(crypto.size() - 1).get("price").toString());
        String complete = url + crypto.get(crypto.size() - 1).get("stock");
        Log.d("IND_PORT", complete);
        final String name = crypto.get(crypto.size() - 1).get("stock").toString();
        final double quantity = Double.parseDouble(crypto.get(crypto.size() - 1).get("quantity").toString());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, complete,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    double cryptoPrice = Double
                            .parseDouble(response.get(response.length() - 1).toString())
                            * quantity;
                    if (cryptoPrice > perf1Price) {
                        perf1Price = cryptoPrice;
                        firstPerf.setText(name);
                    }
                    else if (cryptoPrice > perf2Price) {
                        perf2Price = cryptoPrice;
                        secPerf.setText(name);
                    }
                    totalCrypto += cryptoPrice;

                    double calcReturn = totalCrypto - initCrypto;
                    cryptoInitAUM.setText(cryptoInitAUM.getText() + " " + initCrypto);
                    cryptoCurrAUM.setText(cryptoCurrAUM.getText() + " " + totalCrypto);
                    cryptoReturnText.setText(cryptoReturnText.getText() + " " + calcReturn);
                    cryptoBench.setText(cryptoBench.getText() + " " + (initCrypto * 1.1));

                    totalAUM += totalCrypto;
                    finalReturn += calcReturn;
                    aum.setText("AUM " + totalAUM);
                    totalReturn.setText("Return: " + finalReturn);
                    totalBench.setText("Benchmark Return: " + (finalReturn * 1.1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestSingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /*
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
                                stock1 = Objects.requireNonNull(stocks.get(0).get("stock")).toString();
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

    private class cryptoPriceTask1 extends AsyncTask<String, String, String> {
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                crypto_total = crypto_total + Double.parseDouble(j.get(j.length() - 1).toString());
                                crypto1 = Objects.requireNonNull(crypto.get(0).get("stock")).toString();
                                cprice1 = Double.parseDouble(j.get(j.length() - 1).toString());

                                Double[] price = new Double[9];
                                for (int i = 0; i < j.length(); i++) {
                                    price[i] = Double.parseDouble(j.get(i).toString());
                                }
                                if (crypto1.equals("BTC-USD")) {
                                    CryptoStorage.setBitcoin(price);
                                    Log.d("TAG", "Run1.1: " + Arrays.toString(CryptoStorage.getBitcoin()));
                                } else if (crypto1.equals("ETH-USD")) {
                                    CryptoStorage.setEthereum(price);
                                    Log.d("TAG", "Run2.1: " + Arrays.toString(CryptoStorage.getEthereum()));
                                } else {
                                    CryptoStorage.setDogecoin(price);
                                    Log.d("TAG", "Run3.1: " + Arrays.toString(CryptoStorage.getDogecoin()));
                                }

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
                                stock2 = Objects.requireNonNull(stocks.get(1).get("stock")).toString();
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


    private class cryptoPriceTask2 extends AsyncTask<String, String, String> {
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                crypto_total = crypto_total + Double.parseDouble(j.get(j.length() - 1).toString());
                                crypto2 = Objects.requireNonNull(crypto.get(1).get("stock")).toString();
                                cprice2 = Double.parseDouble(j.get(j.length() - 1).toString());

                                Double[] price = new Double[9];
                                for (int i = 0; i < j.length(); i++) {
                                    price[i] = Double.parseDouble(j.get(i).toString());
                                }
                                if (crypto2.equals("BTC-USD")) {
                                    CryptoStorage.setBitcoin(price);
                                    Log.d("TAG", "Run1.2: " + Arrays.toString(CryptoStorage.getBitcoin()));
                                } else if (crypto2.equals("ETH-USD")) {
                                    CryptoStorage.setEthereum(price);
                                    Log.d("TAG", "Run2.2: " + Arrays.toString(CryptoStorage.getEthereum()));
                                } else {
                                    CryptoStorage.setDogecoin(price);
                                    Log.d("TAG", "Run3.2: " + Arrays.toString(CryptoStorage.getDogecoin()));
                                }

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
                                stock3 = Objects.requireNonNull(stocks.get(2).get("stock")).toString();
                                price3 = Double.parseDouble(j.get("current price").toString());
                                //current_aum2.setText(current_aum2.getText() + ""+crypto_total);

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


    private class cryptoPriceTask3 extends AsyncTask<String, String, String> {
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                crypto_total = crypto_total + Double.parseDouble(j.get(j.length() - 1).toString());
                                crypto3 = Objects.requireNonNull(crypto.get(2).get("stock")).toString();
                                cprice3 = Double.parseDouble(j.get(j.length()-1).toString());

                                Double[] price = new Double[9];
                                for (int i = 0; i < j.length(); i++) {
                                    price[i] = Double.parseDouble(j.get(i).toString());
                                }
                                if (crypto3.equals("BTC-USD")) {
                                    CryptoStorage.setBitcoin(price);
                                    Log.d("TAG", "Run1.3: " + Arrays.toString(CryptoStorage.getBitcoin()));
                                } else if (crypto3.equals("ETH-USD")) {
                                    CryptoStorage.setEthereum(price);
                                    Log.d("TAG", "Run2.3: " + Arrays.toString(CryptoStorage.getEthereum()));
                                } else {
                                    CryptoStorage.setDogecoin(price);
                                    Log.d("TAG", "Run3.3: " + Arrays.toString(CryptoStorage.getDogecoin()));
                                }

                                current_aum2.setText(current_aum2.getText() + ""+crypto_total);
                                double crypto_return = ((crypto_total - total2)/ total2)*100;
                                return2.setText(return2.getText() + "" + crypto_return);

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
    */
}