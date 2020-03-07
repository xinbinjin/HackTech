package com.team2620.coivdashboard.ui.dashboard;

import android.os.Bundle;
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

import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.bean.CountryBean;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ListView countryList = root.findViewById(R.id.countryList);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<CountryBean[]>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });
        return root;
    }
}
