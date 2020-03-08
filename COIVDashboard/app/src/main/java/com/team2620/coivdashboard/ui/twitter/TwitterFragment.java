package com.team2620.coivdashboard.ui.twitter;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.adapter.GoodListAdapter;
import com.team2620.coivdashboard.bean.EmotionBean;
import com.team2620.coivdashboard.bean.GoodsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TwitterFragment extends Fragment {
    private RequestQueue queue = null;
    private PieChart pieChart;
    private  WebView webView;
    private String emotionUrl = "http://35.236.4.22:8080/api/tweet_emotion_analysis?city=";
    private LocationManager locationManager;
    private String locationProvider;
    private String stateNameUrl = "http://35.236.4.22:8080/api/location2cityname?";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_twitter, container, false);
        queue = Volley.newRequestQueue(root.getContext());
        pieChart = root.findViewById(R.id.pie_chart);
        locationManager = (LocationManager) root.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            root.getContext().startActivity(intent);
        }
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(root.getContext(), "No Location Service Found", Toast.LENGTH_SHORT).show();
            return null;
        }
        //获取Location
        final Location location = locationManager.getLastKnownLocation(locationProvider);
        webView = root.findViewById(R.id.webview);
        String content = "<blockquote class=\"twitter-tweet\"><p lang=\"en\" dir=\"ltr\">Amtrak suspends nonstop Acela service between DC and New York due to coronavirus <a href=\"https://t.co/KqHw25K2wm\">https://t.co/KqHw25K2wm</a></p>&mdash; CNBC (@CNBC) <a href=\"https://twitter.com/CNBC/status/1236329570783068161?ref_src=twsrc%5Etfw\">March 7, 2020</a></blockquote> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        content += "<blockquote class=\"twitter-tweet\"><p lang=\"en\" dir=\"ltr\">Hi! You can help stop the spread of Coronavirus in New York City. 1- wash your hands. 2- avoid unnecessary travel and gatherings. 3- mind your neighbors. We can all get through this together- if we change our own behavior, we can save the lives of others and stop the spread.</p>&mdash; Justin Hendrix (@justinhendrix) <a href=\"https://twitter.com/justinhendrix/status/1236654295342297088?ref_src=twsrc%5Etfw\">March 8, 2020</a></blockquote> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";
        addWebViewTwitter(content, webView);
        getLocateStateName(root, location);
        return root;
    }

    public void getLocateStateName(final View view, Location location) {
        stateNameUrl += "lat=" + location.getLatitude() + "&lng=" + location.getLongitude();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stateNameUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        String cityName = gson.fromJson(response, String.class);
                        if (cityName.toLowerCase().contains("los angeles")){
                            getEmotionData(view, "losangeles");
                        } else {
                            getEmotionData(view, "ny");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void getEmotionData(final View view, String location) {
        String url = "http://35.236.4.22:8080/api/tweet_emotion_analysis?city=";
        if (location.equalsIgnoreCase("los angeles")) {
            url += "losangeles";
        } else {
            url += location;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Log.i("json:", response);
                        EmotionBean emotionBean = gson.fromJson(response, EmotionBean.class);
                        List<PieEntry> entryArrayList = new ArrayList<>();
                        entryArrayList.add(new PieEntry(emotionBean.getAnger(), "Anger"));
                        entryArrayList.add(new PieEntry(emotionBean.getDisgust(), "Disgust"));
                        entryArrayList.add(new PieEntry(emotionBean.getFear(), "Fear"));
                        entryArrayList.add(new PieEntry(emotionBean.getJoy(), "Joy"));
                        entryArrayList.add(new PieEntry(emotionBean.getSadness(), "Sadness"));
                        entryArrayList.add(new PieEntry(emotionBean.getSurprise(), "Surprise"));
                        PieDataSet pieDataSet = new PieDataSet(entryArrayList, "Number of Posts");
                        final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 192, 0), Color.rgb(255, 0, 0),
                                Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        for (int c : MY_COLORS) colors.add(c);
                        pieDataSet.setColors(colors);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.animateXY(3000, 3000);
                        pieChart.invalidate();
                        Legend legend = pieChart.getLegend();
                        legend.setTextColor(Color.WHITE);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }
        private void addWebViewTwitter(Object twitterContent, WebView webView) {
            if (twitterContent != null && twitterContent.toString().length() > 0) {
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setAppCacheEnabled(true);
                webView.getSettings().setJavaScriptEnabled(true);
                //webView.getSettings().setPluginsEnabled(true);
                if (Utils.isNetworkAvailable(getContext())) {
                    webView.loadDataWithBaseURL("https://twitter.com", twitterContent.toString(), "text/html", "utf-8", "");
                }

            }
        }
        public void getEmotionData(final View view, String location) {
        String url = "http://35.236.4.22:8080/api/tweet_emotion_analysis?city=";
        if (location.equalsIgnoreCase("los angeles")) {
            url += "losangeles";
        } else {
            url += location;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Log.i("json:", response);
                        EmotionBean emotionBean = gson.fromJson(response, EmotionBean.class);
                        List<PieEntry> entryArrayList = new ArrayList<>();
                        entryArrayList.add(new PieEntry(emotionBean.getAnger(), "Anger"));
                        entryArrayList.add(new PieEntry(emotionBean.getDisgust(), "Disgust"));
                        entryArrayList.add(new PieEntry(emotionBean.getFear(), "Fear"));
                        entryArrayList.add(new PieEntry(emotionBean.getJoy(), "Joy"));
                        entryArrayList.add(new PieEntry(emotionBean.getSadness(), "Sadness"));
                        entryArrayList.add(new PieEntry(emotionBean.getSurprise(), "Surprise"));
                        PieDataSet pieDataSet = new PieDataSet(entryArrayList, "Number of Posts");
                        final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 192, 0), Color.rgb(255, 0, 0),
                                Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};
                        ArrayList<Integer> colors = new ArrayList<Integer>();
                        for (int c : MY_COLORS) colors.add(c);
                        pieDataSet.setColors(colors);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.animateXY(3000, 3000);
                        pieChart.invalidate();
                        Legend legend = pieChart.getLegend();
                        legend.setTextColor(Color.WHITE);
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }
}

    class Utils {
        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
                return true;
            } else {
                return false;
            }
        }
    }