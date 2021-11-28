package com.example.khazaana.main;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.khazaana.R;
import com.example.khazaana.RequestSingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AddStock extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String clientID = AddStockArgs.fromBundle(getArguments()).getClient();

        String user = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference client = db.collection("Authorized IFAs")
                .document(user)
                .collection("Clients")
                .document(clientID);

        String url = "https://finnhub-backend.herokuapp.com/stock/symbols";

        //GET JSONArray via Volley for all stocks
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //parse through array and determine if common stock
                            int stocksATM = response.length();
                            ArrayList<String> stocks = new ArrayList<>();
                            for (int i = 0; i < stocksATM; i++) {
                                JSONObject object = response.getJSONObject(i);
                                if (object.getString("type").equals("Common Stock")) {
                                    stocks.add(object.getString("displaySymbol"));
                                }
                            }

                            //sort array alphabetically
                            Collections.sort(stocks, new Comparator<String>() {
                                @Override
                                public int compare(String s1, String s2) {
                                    return s1.compareToIgnoreCase(s2);
                                }
                            });

                            //add array to dropdown menu for autocomplete entry
                            AutoCompleteTextView auto = view.findViewById(R.id.stockAuto);
                            auto.setAdapter(new ArrayAdapter<>(requireContext(),
                                    R.layout.dropdown_item, stocks));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        //call Singleton for RequestQueue
        RequestSingleton.getInstance(getContext()).addToRequestQueue(request);

        AutoCompleteTextView autoText = view.findViewById(R.id.stockAuto);
        TextInputEditText sharesText = view.findViewById(R.id.field_shares);
        TextInputEditText priceText = view.findViewById(R.id.field_stock_price);

        Button addStock = view.findViewById(R.id.add_stock_button);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autoText.getText().length() <= 0 || sharesText.getText().length() <= 0 ||
                        priceText.getText().length() <= 0) {
                    Toast.makeText(getContext(), "Please input stock details", Toast.LENGTH_SHORT).show();
                }
                else {
                    String auto = autoText.getText().toString();
                    float shares = Float.parseFloat(sharesText.getText().toString());
                    float price = Float.parseFloat(priceText.getText().toString());

                    StockEntry entry = new StockEntry(auto, shares, price);
                    client.update("Stocks", FieldValue.arrayUnion(entry));

                    NavDirections action = AddStockDirections.actionAddStockFragToStockPortfolio();
                    Navigation.findNavController(view).navigate(action);
                }
            }
        });
    }
}