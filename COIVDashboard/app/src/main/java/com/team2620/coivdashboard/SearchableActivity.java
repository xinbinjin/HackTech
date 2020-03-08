package com.team2620.coivdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.team2620.coivdashboard.bean.GoogleLocationBean;

public class SearchableActivity extends AppCompatActivity {

    RequestQueue queue = null;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Intent intent = getIntent();
        String cityName = (String) intent.getExtras().get("cityName");
        queue = Volley.newRequestQueue(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.local_map);
        getLocationByCity(getWindow().getDecorView(), cityName);
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
