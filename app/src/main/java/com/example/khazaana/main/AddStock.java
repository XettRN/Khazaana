package com.example.khazaana.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.khazaana.R;
import com.example.khazaana.RequestSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
                            AutoCompleteTextView auto = view.findViewById(R.id.autoCompleteTextView);
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
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT);
                    }
                });
        //call Singleton for RequestQueue
        RequestSingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}