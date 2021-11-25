package com.example.khazaana.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayPortfolios();
        Button next = view.findViewById(R.id.button18);

        View root = view;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add fragment to bottomnav.xml so this can be written
                NavDirections navDirections = HomeDirections.actionHomeFragToIndividualClientPortfolio();
                Navigation.findNavController(root).navigate(navDirections);
            }
        });
    }

    private void calculateSummary(ArrayList<Home.PortfolioData> data) {
        // TODO: calculate equity summary and total AUM
        int numClients = data.size();
        double aum = 0; // calculate total aum
        TextView numClientsText = getView().findViewById(R.id.numClients);
        TextView aumText = getView().findViewById(R.id.aumSummary);
        numClientsText.setText("Clients: " + numClients);
        aumText.setText("AUM: $" + aum);

        PieChart summaryPie = getView().findViewById(R.id.overallSummaryPie);
        summaryPie.setData(getSummaryPieData(data.get(0).equity)); // need to pass in total amount, not just for one client
        summaryPie.invalidate();

    }

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

    private void displayPortfolios() {
        ArrayList<Home.PortfolioData> data = new ArrayList<Home.PortfolioData>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ifas = db.collection("Authorized IFAs");
        DocumentReference ifa = ifas.document("A5WkIbLiaub1V1bQ9CRwzLdXBSo2");
        CollectionReference clients = ifa.collection("Clients");
        String userID = FirebaseAuth.getInstance().getUid();
        DocumentReference client = clients.document("2KvyW2lzjHclFAHQnTfWFFq2mYS2");


        clients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    List<DocumentSnapshot> clientList = snapshot.getDocuments();
                    for (DocumentSnapshot document : clientList) {
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                            String firstName = (String) document.get("First Name");
                            String lastName = (String) document.get("Last Name");
                            List<Number> equity = (List<Number>) document.get("Equity");
                            double aum = 0; // need to calculate
                            double return_perc = 0;
                            double benchmarkReturn = 0;
                            // TODO: Calculate AUM, return, and benchmark return
                            data.add(new Home.PortfolioData(firstName + " " + lastName, aum,return_perc,
                                    benchmarkReturn, equity));
                            Log.d("TAG", "LIST SIZE: " + data.size());
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    }

                    calculateSummary(data);

                    ListView listView = (ListView) getView().findViewById(R.id.listView);
                    listView.setNestedScrollingEnabled(true);
                    listView.setAdapter(new Home.MyListAdapter(getContext(), R.layout.client_summary_item, data));

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
                mainViewHolder.aum.setText("AUM: $" + getItem(position).aum);
                mainViewHolder.return_perc.setText("Return: " + getItem(position).return_perc + "%");
                mainViewHolder.benchmarkReturn.setText("Benchmark Return: " + getItem(position).benchmarkReturn + "%");
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

        public PortfolioData(String clientName, double aum, double return_perc,
                             double benchmarkReturn, List<Number> equity) {
            this.clientName = clientName;
            this.aum = aum;
            this.return_perc = return_perc;
            this.benchmarkReturn = benchmarkReturn;
            this.equity = equity;
        }
    }
}