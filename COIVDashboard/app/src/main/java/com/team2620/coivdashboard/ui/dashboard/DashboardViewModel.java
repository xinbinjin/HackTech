package com.team2620.coivdashboard.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team2620.coivdashboard.bean.CountryBean;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<CountryBean[]> countryData;

    public DashboardViewModel() {
        countryData = new MutableLiveData<>();
        CountryBean countryBean = new CountryBean();
        countryBean.set_id("China");
        countryBean.setTotalConfirmed(10);
        countryBean.setTotalDeaths(0);
        countryBean.setTotalRecovered(5);
        CountryBean[] countryBeans = new CountryBean[1];
        countryBeans[0] = countryBean;
        countryData.setValue(countryBeans);
    }

    public LiveData<CountryBean[]> getText() {
        return countryData;
    }
}