package com.team2620.coivdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team2620.coivdashboard.adapter.CountryListAdapter;
import com.team2620.coivdashboard.bean.CountryBean;
import com.team2620.coivdashboard.bean.GoogleLocationBean;
import com.team2620.coivdashboard.bean.NearestLocationBean;
import com.team2620.coivdashboard.bean.StateBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    RequestQueue queue = null;
    private SupportMapFragment mapFragment;
    List<List<StateBean>> arrayLists;
    CountryListAdapter countryListAdapter;
    private String countryDataUrl = "http://35.236.4.22:8080/api/covid_data_by_country";
    private ExpandableListView firstCountryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        String cityName = (String) intent.getExtras().get("cityName");
        queue = Volley.newRequestQueue(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.local_map);
        getLocationByCity(getWindow().getDecorView(), cityName);
        TextView textView = findViewById(R.id.current_location);
        textView.setText(cityName);
        getCountryListData(getWindow().getDecorView());
    }

    public void getLocationByCity(final View view, String city)   {
        String url = "http://35.236.4.22:8080/api/location_city?city=" + city;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("message", "onResponse() called with: response = [" + response + "]");
                        Gson gson = new Gson();
                        GoogleLocationBean googleLocationBean = gson.fromJson(response, GoogleLocationBean.class);
                        final double lat = googleLocationBean.getResults().get(0).getGeometry().getLocation().getLat();
                        final double lng = googleLocationBean.getResults().get(0).getGeometry().getLocation().getLng();
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat,lng));
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                                googleMap.moveCamera(center);
                                googleMap.animateCamera(zoom);
                            }
                        });
                        getNearestData(view, lat, lng);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void getNearestData(final View view, double lat, double lng) {
        String url = "http://35.236.4.22:8080/api/nearest_location?lat=" + lat + "&lng=" + lng;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        NearestLocationBean nearestLocationBean = gson.fromJson(response, NearestLocationBean.class);
                        String cityName = nearestLocationBean.get_$ProvinceState84() + ", " + nearestLocationBean.get_$CountryRegion101();
                        TextView localCityName = view.findViewById(R.id.local_city_name);
                        TextView confirmed = view.findViewById(R.id.local_confirmed);
                        TextView death = view.findViewById(R.id.local_death);
                        TextView recovered = view.findViewById(R.id.local_recovered);
                        confirmed.setText(nearestLocationBean.getConfirmed() + "");
                        death.setText(nearestLocationBean.getDeaths() + "");
                        recovered.setText(nearestLocationBean.getRecovered() + "");
                        localCityName.setText(cityName);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void getCountryListData(final View view) {
        arrayLists = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, countryDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        final ArrayList<CountryBean> countryBeans = gson.fromJson(response, new TypeToken<List<CountryBean>>(){}.getType());
                        Comparator<CountryBean> countryBeanComparator = new Comparator<CountryBean>() {
                            @Override
                            public int compare(CountryBean o1, CountryBean o2) {
                                return o2.getTotalConfirmed() - o1.getTotalConfirmed();
                            }
                        };
                        countryBeans.sort(countryBeanComparator);
                        firstCountryListView = view.findViewById(R.id.first_list);
                        for(int i = 0; i < countryBeans.size(); ++i)    {
                            arrayLists.add(new ArrayList<StateBean>());
                        }
                        firstCountryListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                if (parent.isGroupExpanded(groupPosition))  {
                                    parent.collapseGroup(groupPosition);
                                }   else    {
                                    String countryName = countryBeans.get(groupPosition).get_id();
                                    getStateListData(view, countryName, groupPosition);
                                }
                                return true;
                            }
                        });
                        countryListAdapter = new CountryListAdapter(countryBeans,arrayLists, view.getContext());
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

    public void getStateListData(final View view, String countryName, final int groupPosition) {
        String url = "http://35.236.4.22:8080/api/covid_data_by_state?country=" + countryName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ArrayList<StateBean> stateBeans = gson.fromJson(response, new TypeToken<List<StateBean>>() {
                        }.getType());
                        Comparator<StateBean> comparator = new Comparator<StateBean>() {
                            @Override
                            public int compare(StateBean o1, StateBean o2) {
                                return o2.getTotalConfirmed() - o1.getTotalConfirmed();
                            }
                        };
                        stateBeans.sort(comparator);
                        arrayLists.get(groupPosition).clear();
                        arrayLists.get(groupPosition).addAll(stateBeans);
                        countryListAdapter.notifyDataSetChanged();
                        firstCountryListView.expandGroup(groupPosition);
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
