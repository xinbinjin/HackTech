package com.team2620.coivdashboard.ui.twitter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String emotionUrl = "http://35.236.4.22:8080/api/tweet_emotion_analysis?city=";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_twitter, container, false);
        queue = Volley.newRequestQueue(root.getContext());
        pieChart = root.findViewById(R.id.pie_chart);
        getEmotionData(root, "los angeles");
        return root;
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
