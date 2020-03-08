package com.team2620.coivdashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.bean.CountryBean;

import java.util.List;

public class CountryListAdapter extends BaseAdapter {

    private List<CountryBean> countryBeans;
    private LayoutInflater inflater;

    public CountryListAdapter(List<CountryBean> countryBeans, Context context) {
        this.countryBeans = countryBeans;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countryBeans.size();
    }

    @Override
    public CountryBean getItem(int position) {
        return countryBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        View view = inflater.inflate(R.layout.first_list_country_detail_info,null);
        CountryBean countryBean = getItem(position);

        //在view 视图中查找 组件
        TextView textName = view.findViewById(R.id.country_name);
        TextView textConfirmed = view.findViewById(R.id.total_confirmed);
        TextView textDeath = view.findViewById(R.id.total_death);
        TextView textRecovered = view.findViewById(R.id.total_recovered);

        //为Item 里面的组件设置相应的数据
        textName.setText(countryBean.get_id()+"");
        textConfirmed.setText(countryBean.getTotalConfirmed()+"");
        textDeath.setText(countryBean.getTotalDeaths()+"");
        textRecovered.setText(countryBean.getTotalRecovered()+"");

        //返回含有数据的view
        return view;
    }
}
