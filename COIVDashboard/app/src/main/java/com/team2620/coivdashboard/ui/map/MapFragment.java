package com.team2620.coivdashboard.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.bean.CoronaDataBean;

import java.util.List;

public class MapFragment extends Fragment {
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private String locationProvider;
    private RequestQueue queue = null;
    private String RawDataUrl = "http://35.236.4.22:8080/api/covid_daily_data";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //初始化queue
        queue = Volley.newRequestQueue(view.getContext());
        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(view.getContext(), "No Location Service Found", Toast.LENGTH_SHORT).show();
            return null;
        }
        //获取Location
        final Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            Log.i("TAG", "经度" + location.getLongitude() + "纬度" + location.getLatitude());
        }
        mapFragment = SupportMapFragment.newInstance();
        getRawData(view, location);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_view, mapFragment)
                .commit();
        return view;
    }

    public void getRawData(final View view, final Location location) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RawDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        final CoronaDataBean[] coronaDataBeans = gson.fromJson(response, CoronaDataBean[].class);
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                for (int i = 0; i < coronaDataBeans.length; i++) {
                                    CircleOptions circleOptions = new CircleOptions();
                                    circleOptions.center(new LatLng(coronaDataBeans[i].getCoords().getLat(), coronaDataBeans[i].getCoords().getLon()));
                                    circleOptions.radius(20000);
                                    circleOptions.strokeWidth(0);
                                    if (coronaDataBeans[i].getConfirmed() < 20) {
                                        circleOptions.fillColor(0x5500ff00);
                                    } else if (coronaDataBeans[i].getConfirmed() < 100) {
                                        circleOptions.fillColor(0x55dbeb34);
                                    } else {
                                        circleOptions.fillColor(0x55eb3434);
                                    }
                                    googleMap.addCircle(circleOptions);
                                }
                                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                                googleMap.moveCamera(center);
                                googleMap.animateCamera(zoom);
                            }
                        });


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