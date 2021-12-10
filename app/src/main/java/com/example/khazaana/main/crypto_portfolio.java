package com.example.khazaana.main;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.khazaana.CallAPI;
import com.example.khazaana.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class crypto_portfolio extends Fragment {

    RecyclerView recyclerView;
    PieChart pieChart = null;
    List<Number> graph = null;
    List<Map> list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crypto_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.pieChart2);
        graph = new ArrayList<>();
        String clientID = crypto_portfolioArgs.fromBundle(getArguments()).getClientID();
        String ifaID = crypto_portfolioArgs.fromBundle(getArguments()).getIfaID();
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
                        TextView name = view.findViewById(R.id.crypto_client_name);
                        name.setText(doc.get("First Name") + " " + doc.get("Last Name"));

                        list = (List<Map>) doc.get("Crypto");
                        ArrayList<AssetEntry> entries = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            AssetEntry a = new AssetEntry();
                            a.setStock(list.get(i).get("stock").toString());
                            a.setPrice(Float.parseFloat(list.get(i).get("price").toString()));
                            a.setQuantity(Float.parseFloat(list.get(i).get("quantity").toString()));
                            entries.add(a);
                        }

                        recyclerView = view.findViewById(R.id.cryptoRecycler);
                        recyclerView.setAdapter(new crypto_portfolio.CryptoAdapter(entries, getContext()));
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

        Toolbar cryptoBar = view.findViewById(R.id.cryptoBar);
        cryptoBar.inflateMenu(R.menu.asset_toolbar);
        cryptoBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_asset_button) {
                    NavDirections navDirections = crypto_portfolioDirections
                            .actionCryptoPortfolioToAddCrypto(clientID, ifaID);
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                if (i == R.id.delete_asset_button) {
                    NavDirections navDirections = crypto_portfolioDirections
                            .actionCryptoPortfolioToDeleteCrypto(clientID, ifaID);
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                return crypto_portfolio.super.onOptionsItemSelected(item);
            }
        });
    }

    private class CryptoAdapter extends RecyclerView.Adapter<crypto_portfolio.CryptoAdapter.ViewHolder> {
        private final ArrayList<AssetEntry> crypto;
        private final Context context;

        public CryptoAdapter(ArrayList<AssetEntry> list, Context ct) {
            crypto = list;
            context = ct;
        }

        @NonNull
        @Override
        public crypto_portfolio.CryptoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.asset_item, parent, false);

            return new crypto_portfolio.CryptoAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull crypto_portfolio.CryptoAdapter.ViewHolder holder, int position) {
            AssetEntry entry = crypto.get(position);

            CallAPI call = new CallAPI();
            call.calcCrypto(getContext(), entry, new CallAPI.CryptoListener() {
                @Override
                public void OnError(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnResponse(String name, double cryptoPrice, double cryptoReturn) {
                    holder.currPrice.setText(holder.currPrice.getText() + " " + cryptoPrice);
                    holder.assetReturn.setText(holder.assetReturn.getText() + " " + cryptoReturn);
                    graph.add(cryptoPrice);
                    Log.d("Pie chart", "Pie chart data: "+graph);
                    pieChart.setData(getPieData(graph));
                    pieChart.invalidate();
                }
            });
            holder.asset.setText(entry.getStock());
            holder.boughtPrice.setText(holder.boughtPrice.getText() + " " + entry.getPrice());
            holder.owned.setText("Amount: " + entry.getQuantity());
            View root = holder.asset.getRootView();
            holder.asset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add fragment to bottomnav.xml so this can be written
                    NavDirections navDirections = crypto_portfolioDirections.actionCryptoPortfolioToSpecificCrypto((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"), holder.asset.getText().toString());
                    Navigation.findNavController(root).navigate(navDirections);
                }
            });
        }

        @Override
        public int getItemCount() {
            return crypto.size();
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


    private PieData getPieData(List<Number> l) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("Data", "data: "+list);
        for (int i = 0; i < l.size(); i++) {
            entries.add(new PieEntry(l.get(i).floatValue(), "" + list.get(i).get("stock")));
        }


        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }



}