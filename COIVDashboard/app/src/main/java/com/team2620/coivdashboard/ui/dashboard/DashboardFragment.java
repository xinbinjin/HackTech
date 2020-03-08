package com.team2620.coivdashboard.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.adapter.CountryListAdapter;
import com.team2620.coivdashboard.bean.CountryBean;
import com.team2620.coivdashboard.bean.StateBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private ExpandableListView firstCountryListView;
    private RequestQueue queue = null;
    private String countryDataUrl = "http://35.236.4.22:8080/api/covid_data_by_country";
    private String stateDataUrl = "http://35.236.4.22:8080/api/covid_data_by_state?country=";
    private String stateNameUrl = "http://35.236.4.22:8080/api/location2cityname?";
    private LocationManager locationManager;
    private String locationProvider;
    List<List<StateBean>> arrayLists;
    CountryListAdapter countryListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //创建view
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //初始化queue
        queue = Volley.newRequestQueue(root.getContext());

        //location manager
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
        if (location != null) {
            //不为空,显示地理位置经纬度
            Log.i("TAG","经度"+location.getLongitude()+"纬度"+location.getLatitude());
        }
        getLocateStateName(root, location);
        //map
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
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

    public void getStateListData(final View view, String countryName, final int groupPosition)  {
        String url = "http://35.236.4.22:8080/api/covid_data_by_state?country=" + countryName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ArrayList<StateBean> stateBeans = gson.fromJson(response, new TypeToken<List<StateBean>>(){}.getType());
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
    public void getLocateStateName(final View view, Location location) {
        stateNameUrl += "lat=" + location.getLatitude() + "&lng=" + location.getLongitude();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stateNameUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        String cityName = gson.fromJson(response, String.class);
                        Log.i("city name: ", cityName);
                        TextView textView = view.findViewById(R.id.current_location);
                        textView.setText(cityName);
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
