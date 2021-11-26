package com.example.khazaana.main;

import android.os.AsyncTask;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class specific_stock extends Fragment {

    TextView currentP = null;
    TextView priceC = null;
    TextView percentC = null;
    GraphView g = null;
    TextView returnP = null;
    double boughtPrice = 0;
    TextView pe = null;
    TextView pb = null;
    TextView roe = null;
    TextView recommend = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_specific_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        g = view.findViewById(R.id.lineGraph);
        currentP = view.findViewById(R.id.current_price);
        priceC = view.findViewById(R.id.priceChange);
        percentC = view.findViewById(R.id.percentChange);
        TextView sharesOwned = view.findViewById(R.id.sharesOwned);
        TextView buyingPrice = view.findViewById(R.id.buyingPrice);
        returnP = view.findViewById(R.id.returnPercent);
        pe = view.findViewById(R.id.value1);
        pb = view.findViewById(R.id.value2);
        roe = view.findViewById(R.id.value3);
        recommend = view.findViewById(R.id.value4);

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
                        List<Map> t = (List<Map>) document.get("Stocks");
                        sharesOwned.setText("Shares Owned: " + t.get(0).get("quantity"));
                        buyingPrice.setText("Buying Price: " + t.get(0).get("price"));
                        boughtPrice = Double.parseDouble(t.get(0).get("price").toString());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        GridLabelRenderer gridLabel = g.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Price");
        new priceTask().execute("https://finnhub-backend.herokuapp.com/price?symbol=AAPL");
        new tickerTask().execute("https://finnhub-backend.herokuapp.com/ticker?symbol=AAPL");
        new ratiosTask().execute("https://finnhub-backend.herokuapp.com/ratios?symbol=AAPL");
    }

    private class priceTask extends AsyncTask<String, String, String> {
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
                                currentP.setText("$" + j.get("current price"));
                                priceC.setText("$" + j.get("previous price"));
                                percentC.setText("" + j.get("change percentage") + "%");
                                double returnPrice = (((Double) j.get("current price") - boughtPrice)/boughtPrice)*100;
                                returnP.setText("Return: "+returnPrice+"%");
                                if ((Double) j.get("current price") > (Double) j.get("previous price")) {
                                    currentP.setTextColor(getResources().getColor(R.color.green));
                                    priceC.setTextColor(getResources().getColor(R.color.green));
                                    percentC.setTextColor(getResources().getColor(R.color.green));
                                } else {
                                    currentP.setTextColor(getResources().getColor(R.color.red));
                                    priceC.setTextColor(getResources().getColor(R.color.red));
                                    percentC.setTextColor(getResources().getColor(R.color.red));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
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

    private class tickerTask extends AsyncTask<String, String, String> {
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
                            LineGraphSeries<DataPoint> s = null;
                            try {
                                JSONArray array = (JSONArray) j.get("c");
                                s = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                        new DataPoint(10, (Double) array.get(0)),
                                        new DataPoint(11, (Double) array.get(1)),
                                        new DataPoint(12, (Double) array.get(2)),
                                        new DataPoint(13, (Double) array.get(3)),
                                        new DataPoint(14, (Double) array.get(4)),
                                        new DataPoint(15, (Double) array.get(5)),
                                        new DataPoint(16, (Double) array.get(6)),
                                        new DataPoint(17, (Double) array.get(7)),
                                        new DataPoint(18, (Double) array.get(8))

                                });
                                g.addSeries(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
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

    private class ratiosTask extends AsyncTask<String, String, String> {
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
                                pe.setText("" + j.get("p/e"));
                                pb.setText("" + j.get("p/b"));
                                roe.setText("" + j.get("roe"));
                                recommend.setText(""+j.get("recommendation"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
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

}