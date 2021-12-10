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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockPortfolio extends Fragment {
    List<Map> list = null;
    List<Number> graph = null;
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
        pieChart = view.findViewById(R.id.pieChart2);
        graph = new ArrayList<>();
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
                        TextView name = view.findViewById(R.id.crypto_client_name);
                        name.setText(doc.get("First Name") + " " + doc.get("Last Name"));

                        list = (List<Map>) doc.get("Stocks");
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

        Toolbar stockBar = view.findViewById(R.id.stockBar);
        stockBar.inflateMenu(R.menu.asset_toolbar);
        stockBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.add_asset_button) {
                    NavDirections navDirections = (NavDirections) StockPortfolioDirections
                            .actionStockPortfolioToAddStockFrag(clientID, ifaID);
                    Navigation.findNavController(view).navigate(navDirections);
                    return true;
                }
                if (i == R.id.delete_asset_button) {
                    NavDirections navDirections = (NavDirections) StockPortfolioDirections
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
                    graph.add(stockPrice);
                    Log.d("Pie chart", "Pie chart data: "+graph);
                    pieChart.setData(getPieData(graph));
                    pieChart.invalidate();
                }
            });
            holder.asset.setText(entry.getStock());
            holder.boughtPrice.setText(holder.boughtPrice.getText() + " " + entry.getPrice());
            holder.owned.setText("Shares: " + entry.getQuantity());
            View root = holder.asset.getRootView();
            holder.asset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add fragment to bottomnav.xml so this can be written
                    NavDirections navDirections = StockPortfolioDirections.actionStockPortfolioToSpecificStock((String) getArguments().get("clientID"), (String) getArguments().get("ifaID"), holder.asset.getText().toString());
                    Navigation.findNavController(root).navigate(navDirections);
                }
            });
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

    private PieData getPieData(List<Number> l) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Log.d("Data", "data: "+list);
        if (l.size() == 1) {
            entries.add(new PieEntry(l.get(0).floatValue(), "" + list.get(0).get("stock")));
        } else if (l.size() == 2) {
            entries.add(new PieEntry(l.get(0).floatValue(), "" + list.get(0).get("stock")));
            entries.add(new PieEntry(l.get(1).floatValue(), "" + list.get(1).get("stock")));
        } else {
            entries.add(new PieEntry(l.get(0).floatValue(), "" + list.get(0).get("stock")));
            entries.add(new PieEntry(l.get(1).floatValue(), "" + list.get(1).get("stock")));
            entries.add(new PieEntry(l.get(2).floatValue(), "" + list.get(2).get("stock")));
        }


        PieDataSet pieDataSet = new PieDataSet(entries , "");
        pieChart.setEntryLabelColor(getResources().getColor(R.color.black));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setDrawValues(false);

        return new PieData(pieDataSet);
    }
}