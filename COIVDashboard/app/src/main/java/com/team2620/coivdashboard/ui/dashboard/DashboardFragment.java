package com.team2620.coivdashboard.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.adapter.CountryListAdapter;
import com.team2620.coivdashboard.bean.CountryBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private ListView firstCountryListView;
    private RequestQueue queue = null;
    private String countryDataUrl = "http://35.236.4.22:8080/api/covid_data_by_country";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //创建view
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //初始化queue
        queue = Volley.newRequestQueue(root.getContext());

        //map
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }
        });
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.local_map, mapFragment)
                .commit();

        //获取country数据
        getCountryListData(root);
        return root;
    }

    public void getCountryListData(final View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, countryDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ArrayList<CountryBean> countryBeans = gson.fromJson(response, new TypeToken<List<CountryBean>>(){}.getType());
                        Comparator<CountryBean> countryBeanComparator = new Comparator<CountryBean>() {
                            @Override
                            public int compare(CountryBean o1, CountryBean o2) {
                                return o2.getTotalConfirmed() - o1.getTotalConfirmed();
                            }
                        };
                        countryBeans.sort(countryBeanComparator);
                        firstCountryListView = view.findViewById(R.id.first_list);
                        CountryListAdapter countryListAdapter = new CountryListAdapter(countryBeans, view.getContext());
                        firstCountryListView.setAdapter(countryListAdapter);
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
