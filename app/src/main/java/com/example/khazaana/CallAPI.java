package com.example.khazaana;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class CallAPI {

    public interface VolleyResponseListener {
        void OnError(String message);
        void OnResponse(double stockPrice, double stockReturn);
    }
}
