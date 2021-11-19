package com.example.khazaana;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class specific_equity extends AppCompatActivity {

    TextView currentP = null;
    TextView priceC = null;
    TextView percentC = null;
    ImageView change = null;
    GraphView g = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_equity);
        g = findViewById(R.id.lineGraph);
        currentP = findViewById(R.id.current_price);
        priceC = findViewById(R.id.priceChange);
        percentC = findViewById(R.id.percentChange);
        change = findViewById(R.id.change);


        GridLabelRenderer gridLabel = g.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Price");
        new priceTask().execute("https://finnhub-backend.herokuapp.com/price?symbol=AAPL");
        new tickerTask().execute("https://finnhub-backend.herokuapp.com/ticker?symbol=AAPL");
    }

    private class priceTask extends AsyncTask<String, String, String> {
        String data = "";

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                while (line != null) {
                    line = reader.readLine();
                    data = data + line;
                }
                try {
                    JSONObject j = new JSONObject(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                currentP.setText("$" + j.get("current price"));
                                priceC.setText("$" + j.get("previous price"));
                                percentC.setText("" + j.get("change percentage") + "%");
                                if ((Double) j.get("current price") > (Double) j.get("previous price")) {
                                    currentP.setTextColor(getResources().getColor(R.color.green));
                                    priceC.setTextColor(getResources().getColor(R.color.green));
                                    percentC.setTextColor(getResources().getColor(R.color.green));
                                } else {
                                    currentP.setTextColor(getResources().getColor(R.color.red));
                                    priceC.setTextColor(getResources().getColor(R.color.red));
                                    percentC.setTextColor(getResources().getColor(R.color.red));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class tickerTask extends AsyncTask<String, String, String> {
        String data = "";

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";

                while (line != null) {
                    line = reader.readLine();
                    data = data + line;
                }
                try {
                    JSONObject j = new JSONObject(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LineGraphSeries<DataPoint> s = null;
                            try {
                                JSONArray array = (JSONArray) j.get("c");
                                s = new LineGraphSeries<DataPoint>(new DataPoint[]{
                                        new DataPoint(10, (Double) array.get(0)),
                                        new DataPoint(11, (Double) array.get(1)),
                                        new DataPoint(12, (Double) array.get(2)),
                                        new DataPoint(13, (Double) array.get(3)),
                                        new DataPoint(14, (Double) array.get(4)),
                                        new DataPoint(15, (Double) array.get(5)),
                                        new DataPoint(16, (Double) array.get(6)),
                                        new DataPoint(17, (Double) array.get(7)),
                                        new DataPoint(18, (Double) array.get(8))

                                });
                                g.addSeries(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}