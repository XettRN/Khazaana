package com.example.khazaana.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    /*
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
    */
    RecyclerView recyclerView;
    PieChart pieChart = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String clientID = StockPortfolioArgs.fromBundle(getArguments()).getClientID();
        String ifaID = StockPortfolioArgs.fromBundle(getArguments()).getIfaID();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference client = db.collection("Authorized IFAs")
                .document(ifaID)
                .collection("Clients")
                .document(clientID);

        client.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        TextView name = view.findViewById(R.id.stock_client_name);
                        name.setText(doc.get("First Name") + " " + doc.get("Last Name"));

                        List<Map> list = (List<Map>) doc.get("Stocks");
                        ArrayList<AssetEntry> entries = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            AssetEntry a = new AssetEntry();
                            a.setStock(list.get(i).get("stock").toString());
                            a.setPrice(Float.parseFloat(list.get(i).get("price").toString()));
                            a.setQuantity(Float.parseFloat(list.get(i).get("quantity").toString()));
                            entries.add(a);
                        }

                        recyclerView = view.findViewById(R.id.stockRecycler);
                        recyclerView.setAdapter(new StockAdapter(entries, getContext()));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                    else {
                        Log.d("STOCKS", "Document doesn't exist");
                    }
                }
                else {
                    Log.d("STOCKS", "Get failed with", task.getException());
                }
            }
        });

        /*
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

                        new priceTask1().execute("https://finnhub-backend.herokuapp.com/stock/price?symbol="+t.get(0).get("stock"));
                        new priceTask2().execute("https://finnhub-backend.herokuapp.com/stock/price?symbol="+t.get(1).get("stock"));
                        new priceTask3().execute("https://finnhub-backend.herokuapp.com/stock/price?symbol="+t.get(2).get("stock"));

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
                NavDirections navDirections = StockPortfolioDirections.actionStockPortfolioToSpecificStock((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"), ""+t.get(0).get("stock"));
                Navigation.findNavController(root).navigate(navDirections);
            }
        });

        //navigation will have to pass in the client from previous location
        //(from home or client list screen)
        String passedInClientID = (String) getArguments().get("clientID");

        String user = FirebaseAuth.getInstance().getUid();
        DocumentReference clientID = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients")
                .document(passedInClientID);

         */

        Toolbar stockBar = view.findViewById(R.id.stockBar);
        stockBar.inflateMenu(R.menu.asset_toolbar);
        stockBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_asset_button) {
                    NavDirections navDirections = StockPortfolioDirections
                            .actionStockPortfolioToAddStockFrag(clientID, ifaID);
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                if (i == R.id.delete_asset_button) {
                    NavDirections navDirections = StockPortfolioDirections
                            .actionStockPortfolioToDeleteStock(clientID, ifaID);
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                return StockPortfolio.super.onOptionsItemSelected(item);
            }
        });
      

    }

    private class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
        private final ArrayList<AssetEntry> stocks;
        private final Context context;

        public StockAdapter(ArrayList<AssetEntry> list, Context ct) {
            stocks = list;
            context = ct;
        }

        @NonNull
        @Override
        public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.asset_item, parent, false);

            return new StockAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
            AssetEntry entry = stocks.get(position);
            float calcReturn = 0;

            CallAPI call = new CallAPI();
            call.calcStock(getContext(), entry, new CallAPI.StockListener() {
                @Override
                public void OnError(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnResponse(String name, double stockPrice, double stockReturn) {
                    holder.currPrice.setText(holder.currPrice.getText() + " " + stockPrice);
                    holder.assetReturn.setText(holder.assetReturn.getText() + " " + stockReturn);
                }
            });
            holder.asset.setText(entry.getStock());
            holder.boughtPrice.setText(holder.boughtPrice.getText() + " " + entry.getPrice());
            holder.owned.setText("Shares: " + entry.getQuantity());
        }

        @Override
        public int getItemCount() {
            return stocks.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            public TextView asset;
            public TextView boughtPrice;
            public TextView currPrice;
            public TextView owned;
            public TextView assetReturn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                asset = itemView.findViewById(R.id.item_name);
                boughtPrice = itemView.findViewById(R.id.last_bought_price);
                currPrice = itemView.findViewById(R.id.asset_price);
                owned = itemView.findViewById(R.id.assets_owned);
                assetReturn = itemView.findViewById(R.id.asset_return);
            }
        }
    }


    /*
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
    */

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