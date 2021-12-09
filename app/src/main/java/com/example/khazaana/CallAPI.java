package com.example.khazaana;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khazaana.main.AssetEntry;

import org.json.JSONException;
import org.json.JSONObject;

public class CallAPI {

    public interface StockListener {
        void OnError(String message);
        void OnResponse(String name, double stockPrice, double stockReturn);
    }

    public void calcStock(Context context, AssetEntry stock, StockListener listener) {
        String url = "https://finnhub-backend.herokuapp.com/stock/price?symbol=";
        String complete = url + stock.getStock();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complete,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double stockPrice = response.getDouble("current price");
                    double stockReturn = stockPrice - stock.getPrice();

                    listener.OnResponse(stock.getStock(), stockPrice, stockReturn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.OnError("Error getting stock price");
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }
}
