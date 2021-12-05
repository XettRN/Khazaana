package com.example.khazaana.main;

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
import android.widget.TextView;

import com.example.khazaana.R;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class crypto_portfolio extends Fragment {

    List<Map> t = null;
    List<Number> crypto = null;
    PieChart pieChart = null;
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
        currentP1 = view.findViewById(R.id.ccurrentPrice1);
        return1 = view.findViewById(R.id.creturn1);

        TextView crypto2 = view.findViewById(R.id.crypto2);
        TextView boughtPrice2 = view.findViewById(R.id.cboughtPrice2);
        TextView cryptoOwned2 = view.findViewById(R.id.cryptoOwned2);
        currentP2 = view.findViewById(R.id.ccurrentPrice2);
        return2 = view.findViewById(R.id.creturn2);

        TextView crypto3 = view.findViewById(R.id.crypto3);
        TextView boughtPrice3 = view.findViewById(R.id.cboughtPrice3);
        TextView cryptoOwned3 = view.findViewById(R.id.cryptoOwned3);
        currentP3 = view.findViewById(R.id.ccurrentPrice3);
        return3 = view.findViewById(R.id.creturn3);

        pieChart = view.findViewById(R.id.pieChart2);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.getLegend().setEnabled(false);

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document("A5WkIbLiaub1V1bQ9CRwzLdXBSo2");
        CollectionReference clients = ifa.collection("Clients");
        DocumentReference client = clients.document("24pLjJbK43clJtggGDLPk9ALQfZ2");*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document((String) getArguments().get("ifaID"));
        CollectionReference clients = ifa.collection("Clients");
        DocumentReference client = clients.document((String) getArguments().get("clientID"));

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
                        buyingPrice1 = Double.parseDouble(t.get(0).get("price").toString());
                        q1 = Double.parseDouble(t.get(0).get("quantity").toString());

                        crypto2.setText("Crypto: " + t.get(1).get("stock"));
                        boughtPrice2.setText("Bought Price: " + t.get(1).get("price"));
                        cryptoOwned2.setText("Crypto Owned: " + t.get(1).get("quantity"));
                        buyingPrice2 = Double.parseDouble(t.get(1).get("price").toString());
                        q2 = Double.parseDouble(t.get(1).get("quantity").toString());

                        crypto3.setText("Crypto: " + t.get(2).get("stock"));
                        boughtPrice3.setText("Bought Price: " + t.get(2).get("price"));
                        cryptoOwned3.setText("Crypto Owned: " + t.get(2).get("quantity"));
                        buyingPrice3 = Double.parseDouble(t.get(2).get("price").toString());
                        q3 = Double.parseDouble(t.get(2).get("quantity").toString());

                        new priceTask1().execute("https://finnhub-backend.herokuapp.com/crypto/ticker?symbol="+t.get(0).get("stock"));
                        new priceTask2().execute("https://finnhub-backend.herokuapp.com/crypto/ticker?symbol="+t.get(1).get("stock"));
                        new priceTask3().execute("https://finnhub-backend.herokuapp.com/crypto/ticker?symbol="+t.get(2).get("stock"));

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
      


        View root = view;
        crypto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = crypto_portfolioDirections.actionCryptoPortfolioToSpecificCrypto((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"),""+t.get(2).get("stock"));
                Navigation.findNavController(root).navigate(navDirections);
            }
        });
      
        //navigation will have to pass in the client from previous location
        //(from home or client list screen)
        String passedInClientID = "EJCyh9saTPZ9YzHvdtdN";

        String user = FirebaseAuth.getInstance().getUid();
        DocumentReference clientID = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients")
                .document(passedInClientID);

        Toolbar cryptoBar = view.findViewById(R.id.cryptoBar);
        cryptoBar.inflateMenu(R.menu.asset_toolbar);
        cryptoBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_asset_button) {
                    NavDirections navDirections = crypto_portfolioDirections
                            .actionCryptoPortfolioToAddCrypto(clientID.getId());
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                return crypto_portfolio.super.onOptionsItemSelected(item);
            }
        });
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() { //add getActivity. before this method
                        @Override
                        public void run() {
                            try {
                                currentP1.setText("Current Price: $" + j.get(j.length() - 1));
                                cp1 = Double.parseDouble(j.get(j.length() - 1).toString());
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP2.setText("Current Price: $" + j.get(j.length() - 1));
                                cp2 = Double.parseDouble(j.get(j.length() - 1).toString());
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
                    JSONArray j = new JSONArray(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP3.setText("Current Price: $" + j.get(j.length() - 1));
                                cp3 = Double.parseDouble(j.get(j.length() - 1).toString());
                                double returnS3 = ((cp3 - buyingPrice3)/buyingPrice3)*100;
                                return3.setText("Return: "+returnS3+"%");


                                double total = cp1*q1 + cp2*q2 + cp3*q3;
                                double invst1 = cp1*q1 / total;
                                double invst2 = cp2*q2 / total;
                                double invst3 = cp3*q3 / total;

                                crypto = new ArrayList<>();
                                crypto.add(invst1);
                                crypto.add(invst2);
                                crypto.add(invst3);
                                pieChart.setData(getPieData(crypto));
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
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }

}