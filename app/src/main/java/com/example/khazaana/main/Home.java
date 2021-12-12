package com.example.khazaana.main;

import android.content.Context;
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
import android.widget.ArrayAdapter;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Home extends Fragment {
    private NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayPortfolios();
<<<<<<< HEAD
=======

>>>>>>> 0981b64263018d42ffe6761933080ef015b6e1c5
    }


    // calculates summary of aum, benchmark, etc
    private void calculateSummary(ArrayList<Home.PortfolioData> data) {
        // TODO: calculate equity summary
        LayoutInflater factory = getLayoutInflater();
        View homeHeader = factory.inflate(R.layout.fragment_home_summary, null);
        int numClients = data.size();
        double aum = 0; // calculate total aum
        for (Home.PortfolioData item : data) {
            aum += item.aum;
        }

        TextView numClientsText = homeHeader.findViewById(R.id.numClients);
        TextView aumText = homeHeader.findViewById(R.id.aumSummary);
        PieChart summaryPie = homeHeader.findViewById(R.id.overallSummaryPie);
        TextView top5 = homeHeader.findViewById(R.id.topPerformers);

        if (data.size() == 0) {
            numClientsText.setText("Clients: 0");
            aumText.setText("AUM: " + format.format(0));
            List<Number> pieData = new ArrayList<>();
            pieData.add(0);
            pieData.add(100);
            summaryPie.setData(getSummaryPieData(pieData)); // need to pass in total amount, not just for one client
            summaryPie.invalidate();
            top5.setVisibility(View.INVISIBLE);


        } else {
            top5.setVisibility(View.VISIBLE);
            numClientsText.setText("Clients: " + numClients);
            aumText.setText("AUM: " + format.format(aum));

            double equitySum = 0;
            double debtSum = 0;
            for (PortfolioData element : data) {
                equitySum += (long) element.equity.get(0);
                debtSum += (long) element.equity.get(1);
            }
            List<Number> list = new ArrayList<>();
            list.add((long) (equitySum/numClients));
            list.add( (long) (debtSum/numClients));

            summaryPie.setData(getSummaryPieData(list)); // need to pass in total amount, not just for one client
            summaryPie.invalidate();
        }


        ListView listView = (ListView) getView().findViewById(R.id.listView);
        listView.addHeaderView(homeHeader, null, false);

    }
    // Calculates pie chart values needed for summary
    private PieData getSummaryPieData(List<Number> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(list.get(0).floatValue(), "Equity"));
        entries.add(new PieEntry(list.get(1).floatValue(), "Debt"));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        //int[] colors = {Color.rgb(245, 187, 0), Color.rgb(255, 253, 208)}; // Custom Colors for chart
        //pieDataSet.setColors(ColorTemplate.createColors(colors));
        pieDataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }

    // Finds the top 5 portfolios to display given a list of client portfolio data
    private ArrayList<Home.PortfolioData> filterPortfolios(ArrayList<Home.PortfolioData> data) {
        if (data.size() <= 5) {
            return data;
        } else {
            Collections.sort(data, new Comparator<PortfolioData>() {
                @Override
                public int compare(PortfolioData portfolioData, PortfolioData t1) {
                    if (portfolioData.aum < t1.aum)
                        return 1;
                    else if (portfolioData.aum > t1.aum)
                        return -1;
                    else
                        return 0;
                }
            });
            ArrayList<Home.PortfolioData> filterdList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                filterdList.add(data.get(i));
            }
            return filterdList;
        }
    }

    private double calculateAUM(List<Map> stocks) {
        if (stocks != null) {
            double totalS = 0;
            for (int i = 0; i < stocks.size(); i++) {
                totalS = totalS + Double.parseDouble(stocks.get(i).get("price").toString()) * Double.parseDouble(stocks.get(i).get("quantity").toString());
            }
            return totalS;
        }
        return 0;
    }

    double initStock = 0;
    double totalStock = 0;
    double initAUM = 0;
    double totalAUM = 0;
    double returnStock = 0;
    double finalReturn = 0;
    double benchmarkReturn = 10;

    // Displays top 5 portfolios
    private void displayPortfolios() {
        ArrayList<Home.PortfolioData> data = new ArrayList<Home.PortfolioData>();
        String user = FirebaseAuth.getInstance().getUid();
        assert user != null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cliRef = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients");


        cliRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    List<DocumentSnapshot> clientList = snapshot.getDocuments();
                    int index = 0;
                    for (DocumentSnapshot document : clientList) {
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                            String firstName = (String) document.get("First Name");
                            String lastName = (String) document.get("Last Name");
                            List<Map> stocks = (List<Map>) document.get("Stocks");
                            List<Number> equity = (List<Number>) document.get("Equity");

                            CallAPI callAPI = new CallAPI();

                            //calculations and api calls
                            if (stocks != null && stocks.size() > 0) {
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
                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void OnResponse(String name, double stockPrice, double stockReturn) {

                                            totalStock += (stockPrice * a.getQuantity());
                                            totalAUM += (stockPrice * a.getQuantity());
                                            returnStock += (stockReturn * a.getQuantity());
                                            finalReturn += (stockReturn * a.getQuantity());
                                            data.add(new Home.PortfolioData(firstName + " " + lastName, totalAUM, returnStock,
                                                    benchmarkReturn, equity, document.getId()));


                                        }
                                    });
                                }
                            } else {
                                data.add(new Home.PortfolioData(firstName + " " + lastName, totalAUM, returnStock,
                                        benchmarkReturn, equity, document.getId()));
                            }

                            // TODO: figure out return and benchmark return
//                            data.add(new Home.PortfolioData(firstName + " " + lastName, totalAUM, returnStock,
//                                    benchmarkReturn, equity, document.getId()));
                            Log.d("TAG", "LIST SIZE: " + data.size());
                            index++;
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    }

                    ArrayList<Home.PortfolioData> top5Data = filterPortfolios(data);
                    calculateSummary(data);

                    ListView listView = (ListView) getView().findViewById(R.id.listView);
                    listView.setAdapter(new Home.MyListAdapter(getContext(), R.layout.ifa_home_client_summary_item, top5Data));

                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private PieData getPieData(List<Number> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(list.get(0).floatValue(), "Equity"));
        entries.add(new PieEntry(list.get(1).floatValue(), "Debt"));

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }


    private class MyListAdapter extends ArrayAdapter<Home.PortfolioData> {
        private int layout;
        private MyListAdapter(@NonNull Context context, int resource, @NonNull List<Home.PortfolioData> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Home.ViewHolder mainViewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                Home.ViewHolder viewHolder = new Home.ViewHolder();
                viewHolder.clientName = (TextView) convertView.findViewById(R.id.client_name);
                viewHolder.pieChart = (PieChart) convertView.findViewById(R.id.pieChart);
                viewHolder.aum = (TextView) convertView.findViewById(R.id.aum);
                viewHolder.return_perc = (TextView) convertView.findViewById(R.id.return_percent);
                viewHolder.benchmarkReturn = (TextView) convertView.findViewById(R.id.bench_return);
                convertView.setTag(viewHolder);
            } else {
                mainViewHolder = (Home.ViewHolder) convertView.getTag();
                mainViewHolder.clientName.setText(getItem(position).clientName);
                mainViewHolder.pieChart.setData(getPieData(getItem(position).equity));
                mainViewHolder.pieChart.invalidate();
                mainViewHolder.aum.setText("AUM: " + format.format(getItem(position).aum));
                mainViewHolder.return_perc.setText("Return: " + getItem(position).return_perc + "%");
                mainViewHolder.benchmarkReturn.setText("Benchmark Return: " + getItem(position).benchmarkReturn + "%");
                View root = mainViewHolder.clientName.getRootView();
                mainViewHolder.clientName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add fragment to bottomnav.xml so this can be written
                        NavDirections navDirections = HomeDirections.
                                actionHomeFragToIndividualClientPortfolio(getItem(position).cliendId, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Navigation.findNavController(root).navigate(navDirections);
                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView clientName;
        PieChart pieChart;
        TextView aum;
        TextView return_perc;
        TextView benchmarkReturn;
    }


    public class PortfolioData {
        public String clientName;
        public double aum;
        public double return_perc;
        public double benchmarkReturn;
        public List<Number> equity;
        public String cliendId;

        public PortfolioData(String clientName, double aum, double return_perc,
                             double benchmarkReturn, List<Number> equity, String clientId) {
            this.clientName = clientName;
            this.aum = aum;
            this.return_perc = return_perc;
            this.benchmarkReturn = benchmarkReturn;
            this.equity = equity;
            this.cliendId = clientId;
        }
    }
}