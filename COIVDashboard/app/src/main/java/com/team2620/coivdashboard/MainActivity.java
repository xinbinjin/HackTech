package com.team2620.coivdashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.team2620.coivdashboard.bean.AutoCompleteBean;
import com.team2620.coivdashboard.ui.map.MapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SearchView searchView = null;
    SearchView.SearchAutoComplete searchAutoComplete = null;
    private  ArrayAdapter arrayAdapter;
    private List<String> CITIES = null;
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);
        CITIES = new ArrayList<>();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_map, R.id.navigation_twitter, R.id.navigation_supplies)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CITIES);
        searchAutoComplete.setAdapter(arrayAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("options", "onItemClick: " + arrayAdapter.getItem(position).toString());
                searchView.setQuery(arrayAdapter.getItem(position).toString(),true);
            }
        });
        searchView.setBackgroundColor(Color.parseColor("#1E1E1E"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
                intent.putExtra("cityName", s);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("message", "onQueryTextChange: " + s);
                updateAutoComplete(s);
                return false;
            }
        });
        return true;
    }

    public void updateAutoComplete(String input)    {
        if (input.length() > 0) {
            String url = "http://35.236.4.22:8080/api/autocomplete?input=" + input;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            AutoCompleteBean autoCompleteBean = gson.fromJson(response, AutoCompleteBean.class);
                            if (autoCompleteBean.getStatus().equalsIgnoreCase("OK")) {
                                List<AutoCompleteBean.PredictionsBean> predictionsBeans = autoCompleteBean.getPredictions();
                                CITIES.clear();
                                arrayAdapter.clear();
                                for (int i = 0; i < predictionsBeans.size(); ++i) {
                                    CITIES.add(predictionsBeans.get(i).getDescription());
                                    arrayAdapter.add(CITIES.get(i));
                                }
                                Log.i("testing", "onResponse: " + CITIES.get(0));
                                arrayAdapter.getFilter().filter(searchAutoComplete.getText(), null);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", "onErrorResponse: ");
                }
            });
            queue.add(stringRequest);
        } else  {
            CITIES.clear();
            arrayAdapter.clear();
            arrayAdapter.notifyDataSetChanged();
        }
    }

}
