package com.example.khazaana;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.khazaana.main.AssetEntry;

import org.json.JSONArray;
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

    public interface CryptoListener {
        void OnError(String message);
        void OnResponse(String name, double cryptoPrice, double cryptoReturn, Double[] prices);
    }

    public void calcCrypto(Context context, AssetEntry crypto, CryptoListener listener) {
        String url = "https://finnhub-backend.herokuapp.com/crypto/ticker?symbol=";
        String complete = url + crypto.getStock();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, complete,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    double cryptoPrice = Double
                            .parseDouble(response.get(response.length() - 1).toString());
                    double cryptoReturn = cryptoPrice - crypto.getPrice();
                    Double [] prices = new Double[response.length()];
                    for (int i = 0; i < prices.length; i++) {
                        prices[i] = Double.parseDouble(response.get(i).toString());
                    }

                    listener.OnResponse(crypto.getStock(), cryptoPrice, cryptoReturn, prices);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //listener.OnError("Error getting crypto price");
            }
        });
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }
}
