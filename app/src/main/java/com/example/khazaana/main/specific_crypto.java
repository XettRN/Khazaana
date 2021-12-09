package com.example.khazaana.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.khazaana.LoadScreen;
import com.example.khazaana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CryptoPrimitive;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class specific_crypto extends Fragment {

    TextView currentP = null;
    TextView priceC = null;
    TextView percentC = null;
    GraphView g = null;
    TextView returnP = null;
    double boughtPrice = 0;
    DecimalFormat d = new DecimalFormat("#.###");
    TextView cName = null;
    Button sout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_crypto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        g = view.findViewById(R.id.clineGraph);
        currentP = view.findViewById(R.id.ccurrent_price);
        priceC = view.findViewById(R.id.cpriceChange);
        percentC = view.findViewById(R.id.cpercentChange);
        TextView cryptoOwned = view.findViewById(R.id.cryptoOwned);
        TextView buyingPrice = view.findViewById(R.id.cbuyingPrice);
        returnP = view.findViewById(R.id.creturnPercent);
        cName = view.findViewById(R.id.cryp_name);
        sout = view.findViewById(R.id.sout2);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document((String) getArguments().get("ifaID"));
        CollectionReference clients = ifa.collection("Clients");
        DocumentReference client = clients.document((String) getArguments().get("clientID"));
        Log.d("TAG", "Crypto Name" + getArguments().get("crypto_name"));

        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map> t = (List<Map>) document.get("Crypto");
                        String cryptoName = (String) getArguments().get("crypto_name");
                        for (int i = 0; i < t.size(); i++) {
                            Log.d("TAG", "Current Crypto" + t.get(i).get("stock"));
                            if (Objects.requireNonNull(t.get(i).get("stock")).toString().equals(cryptoName)) {
                                cName.setText(""+getArguments().get("crypto_name"));
                                Log.d("TAG", "Current Crypto from firebase" + t.get(i).get("stock"));
                                Log.d("TAG", "Current Crypto from previous screen" + cryptoName);
                                cryptoOwned.setText("Crypto Owned: " + t.get(i).get("quantity"));
                                buyingPrice.setText("Buying Price: " + t.get(i).get("price"));
                                boughtPrice = Double.parseDouble(t.get(i).get("price").toString());
                            }
                        }


                        GridLabelRenderer gridLabel = g.getGridLabelRenderer();
                        gridLabel.setHorizontalAxisTitle("Days");
                        gridLabel.setVerticalAxisTitle("Price");


                        Double[] cryptoTicker;
                        if (cryptoName.equals("ETH-USD")) {
                            cryptoTicker = CryptoStorage.getEthereum();
                        } else if (cryptoName.equals("BTC-USD")) {
                            cryptoTicker = CryptoStorage.getBitcoin();
                        } else {
                            cryptoTicker = CryptoStorage.getDogecoin();
                        }

                        currentP.setText("$" + d.format(cryptoTicker[cryptoTicker.length - 1]));
                        priceC.setText("$" + d.format(cryptoTicker[cryptoTicker.length - 2]));
                        String cp = cryptoTicker[cryptoTicker.length - 1].toString();
                        String pp = cryptoTicker[cryptoTicker.length - 2].toString();
                        double currP = Double.parseDouble(cp);
                        double prevP = Double.parseDouble(pp);
                        double percent = (currP - prevP) / prevP;
                        percentC.setText("" + d.format(percent * 100) + "%");
                        double returnPrice = ((currP - boughtPrice) / boughtPrice) * 100;
                        returnP.setText("Return: " + returnPrice + "%");

                        if (currP > prevP) {
                            currentP.setTextColor(getResources().getColor(R.color.green));
                            priceC.setTextColor(getResources().getColor(R.color.green));
                            percentC.setTextColor(getResources().getColor(R.color.green));
                        } else {
                            currentP.setTextColor(getResources().getColor(R.color.red));
                            priceC.setTextColor(getResources().getColor(R.color.red));
                            percentC.setTextColor(getResources().getColor(R.color.red));
                        }

                        LineGraphSeries<DataPoint> s = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                new DataPoint(0, cryptoTicker[0]),
                                new DataPoint(1, cryptoTicker[1]),
                                new DataPoint(2, cryptoTicker[2]),
                                new DataPoint(3, cryptoTicker[3]),
                                new DataPoint(4, cryptoTicker[4]),
                                new DataPoint(5, cryptoTicker[5]),
                                new DataPoint(6, cryptoTicker[6]),
                                new DataPoint(7, cryptoTicker[7]),
                                new DataPoint(8, cryptoTicker[8])
                        });

                        g.addSeries(s);

//                        new tickerTask().execute("https://finnhub-backend.herokuapp.com/crypto/ticker?symbol="+getArguments().get("cryptoName"));

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoadScreen.class));
            }
        });
    }
}

//    private class tickerTask extends AsyncTask<String, String, String> {
//        String data = "";
//
//        protected String doInBackground(String... params) {
//
//
//            HttpURLConnection connection = null;
//            BufferedReader reader = null;
//            try {
//                URL url = new URL(params[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//
//                InputStream stream = connection.getInputStream();
//
//                reader = new BufferedReader(new InputStreamReader(stream));
//
//                String line = "";
//
//                while (line != null) {
//                    line = reader.readLine();
//                    data = data + line;
//                }
//                try {
//                    JSONArray j = new JSONArray(data);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                            currentP.setText("$" + d.format(j.get(j.length() - 1)));
//                            priceC.setText("$" + d.format(j.get(j.length() - 3)));
//                            String cp = j.get(j.length() - 1).toString();
//                            String pp = j.get(j.length() - 3).toString();
//                            double currP = Double.parseDouble(cp);
//                            double prevP = Double.parseDouble(pp);
//                            double percent = (currP - prevP)/ prevP;
//                            percentC.setText("" + d.format(percent*100) + "%");
//                            double returnPrice = ((currP - boughtPrice)/boughtPrice)*100;
//                            returnP.setText("Return: "+returnPrice+"%");
//                            if (currP > prevP) {
//                                currentP.setTextColor(getResources().getColor(R.color.green));
//                                priceC.setTextColor(getResources().getColor(R.color.green));
//                                percentC.setTextColor(getResources().getColor(R.color.green));
//                            } else {
//                                currentP.setTextColor(getResources().getColor(R.color.red));
//                                priceC.setTextColor(getResources().getColor(R.color.red));
//                                percentC.setTextColor(getResources().getColor(R.color.red));
//                            }
//                            LineGraphSeries<DataPoint> s = null;
//
//                                s = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                                        new DataPoint(0, Double.parseDouble(j.get(0).toString())),
//                                        new DataPoint(1, Double.parseDouble(j.get(1).toString())),
//                                        new DataPoint(2, Double.parseDouble(j.get(2).toString())),
//                                        new DataPoint(3, Double.parseDouble(j.get(3).toString())),
//                                        new DataPoint(4, Double.parseDouble(j.get(4).toString())),
//                                        new DataPoint(5, Double.parseDouble(j.get(5).toString())),
//                                        new DataPoint(6, Double.parseDouble(j.get(6).toString())),
//                                        new DataPoint(7, Double.parseDouble(j.get(7).toString())),
//                                        new DataPoint(8, Double.parseDouble(j.get(8).toString()))
//                                });
//                                g.addSeries(s);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//    }
//}