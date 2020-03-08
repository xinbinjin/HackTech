package com.team2620.coivdashboard.ui.supplies;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.adapter.GoodListAdapter;
import com.team2620.coivdashboard.bean.GoodsBean;
import com.team2620.coivdashboard.bean.StateBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SuppliesFragment extends Fragment {

    private RequestQueue queue = null;
    private ArrayList<GoodsBean> goodsBeans;
    private GoodListAdapter goodListAdapter;
    private ListView goodListView;
    private String eBaySearchUrl = "https://www.ebay.com/sch/i.html?_nkw=";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_supplies, container, false);
        queue = Volley.newRequestQueue(root.getContext());
        goodListView = root.findViewById(R.id.good_list);
        getEbayData(root);
        goodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsBean goodsBean = (GoodsBean)goodListAdapter.getItem(position);
                String url = eBaySearchUrl + goodsBean.getKeyword();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return root;
    }

    public void getEbayData(final View view)    {
        String url = "http://35.236.4.22:8080/api/ebay_analysis";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ArrayList<GoodsBean> goodsBeans = gson.fromJson(response, new TypeToken<List<GoodsBean>>(){}.getType());
                        goodListAdapter = new GoodListAdapter(goodsBeans, view.getContext());
                        goodListView.setAdapter(goodListAdapter);
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
