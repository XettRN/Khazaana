package com.example.khazaana.main.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClientHome extends Fragment {
    private NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayPortfolio();
        // TODO: Create list of performers
        ArrayList<String> performers = new ArrayList<>();
        performers.add("MSFT: 341.34");
        performers.add("AMZ: 38.343");
        performers.add("AAPL: 5.86");
        performers.add("GOOG: 84.80");
        performers.add("TSLA: 42.74");

        ArrayList<String> laggards = new ArrayList<>();
        laggards.add("DUK: 101.17");
        laggards.add("UBER: 38.08");
        laggards.add("AAPL: 5.86");
        laggards.add("SO: 64.47");
        laggards.add("MAR: 156.53");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.performers);
        rv.setLayoutManager(layoutManager);
        RecycleViewAdapter radapter = new RecycleViewAdapter(getContext(), performers);
        rv.setAdapter(radapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rv2 = (RecyclerView) getView().findViewById(R.id.laggards);
        rv2.setLayoutManager(layoutManager2);
        RecycleViewAdapter radapter2= new RecycleViewAdapter(getContext(), laggards);
        rv2.setAdapter(radapter2);

    }

    private class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
        private ArrayList<String> stocks = new ArrayList<>();
        public RecycleViewAdapter(Context context, ArrayList<String> performers) {
            stocks = performers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_home_performers, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.stock.setText(stocks.get(position));
        }

        @Override
        public int getItemCount() {
            return stocks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            Button stock;
            public ViewHolder(View itemView) {
                super(itemView);
                stock = itemView.findViewById(R.id.crypto_name);
            }
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

    // Displays top 5 portfolios
    private void displayPortfolio() {
        ArrayList<String> data = new ArrayList<>();
        String user = FirebaseAuth.getInstance().getUid();
        assert user != null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cliRef = db.collection("Client List")
                .document(user);


        cliRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                        String firstName = (String) document.get("First Name");
                        String lastName = (String) document.get("Last Name");
                        List<Map> stocks = (List<Map>) document.get("Stocks");
                        List<Number> equity = (List<Number>) document.get("Equity");

                        double aum = calculateAUM(stocks); // need to calculate
                        double return_perc = 23; // TODO: Calculate return
                        double benchmarkReturn = 10;

                        TextView clientName = (TextView) getView().findViewById(R.id.welcome);
                        PieChart chart = (PieChart) getView().findViewById(R.id.overallSummaryPie);
                        TextView aumText = (TextView) getView().findViewById(R.id.aumSummary);
                        TextView returnText = (TextView) getView().findViewById(R.id.returns);
                        TextView benchmarkText = (TextView) getView().findViewById(R.id.benchmarkReturn);

                        clientName.setText("Welcome, " + firstName + " " + lastName);
                        chart.setData(getPieData(equity));
                        chart.invalidate();
                        aumText.setText("AUM: " + format.format(aum));
                        returnText.setText("Return: " + return_perc + "%");
                        benchmarkText.setText("Benchmark Return: " + benchmarkReturn + "%");

                    } else {
                        Log.d("TAG", "No such document");
                    }


                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private PieData getPieData(List<Number> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (entries.size() == 0) {
            entries.add(new PieEntry(100, "Equity"));
            entries.add(new PieEntry(0, "Debt"));
        } else {
            entries.add(new PieEntry(list.get(0).floatValue(), "Equity"));
            entries.add(new PieEntry(list.get(1).floatValue(), "Debt"));
        }

        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }


//    private class ListViewAdapter extends ArrayAdapter<String> {
//        private int layout;
//        private ListViewAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
//            super(context, resource, objects);
//            layout = resource;
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            ClientHome.ViewHolder mainViewHolder = null;
//            if (convertView == null) {
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                convertView = inflater.inflate(layout, parent, false);
//                ClientHome.ViewHolder viewHolder = new ClientHome.ViewHolder();
//                viewHolder.stock = (Button) convertView.findViewById(R.id.stock_name);
//
//                convertView.setTag(viewHolder);
//            } else {
//                mainViewHolder = (ClientHome.ViewHolder) convertView.getTag();
//                mainViewHolder.stock.setText(getItem(position));
//
//            }
//            return convertView;
//        }
//    }




}