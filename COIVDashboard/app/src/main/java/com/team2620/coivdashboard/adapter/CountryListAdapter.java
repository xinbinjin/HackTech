package com.team2620.coivdashboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.bean.CountryBean;
import com.team2620.coivdashboard.bean.StateBean;

import java.util.List;

public class CountryListAdapter extends BaseExpandableListAdapter {

    private List<CountryBean> countryBeans;
    private List<List<StateBean>> stateBeans;
    private Context mContext;

    public CountryListAdapter(List<CountryBean> countryBeans, List<List<StateBean>> stateBeans, Context context) {
        this.countryBeans = countryBeans;
        this.stateBeans = stateBeans;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return countryBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stateBeans.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return countryBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return stateBeans.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        GroupHolder holder = null;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.country_detail_list, null);
            holder.countryName = view.findViewById(R.id.country_name);
            holder.totalConfirmed = view.findViewById(R.id.country_confirmed);
            holder.totalDeath = view.findViewById(R.id.country_death);
            holder.totalRecovered = view.findViewById(R.id.country_recovered);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        CountryBean countryBean = (CountryBean) getGroup(groupPosition);
        holder.countryName.setText(countryBean.get_id());
        holder.totalConfirmed.setText(countryBean.getTotalConfirmed() + "");
        holder.totalDeath.setText(countryBean.getTotalDeaths() + "");
        holder.totalRecovered.setText(countryBean.getTotalRecovered() + "");

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.state_detail_list, null);
            holder.stateName = view.findViewById(R.id.state_name);
            holder.totalConfirmed = view.findViewById(R.id.state_confirmed);
            holder.totalDeath = view.findViewById(R.id.state_death);
            holder.totalRecovered = view.findViewById(R.id.state_recovered);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        StateBean stateBean = stateBeans.get(groupPosition).get(childPosition);
        if (stateBean.get_id() != "" && stateBean.get_id() != null) {
            holder.stateName.setText(stateBean.get_id());
            Log.i("_id:", stateBean.get_id());
            holder.totalConfirmed.setText(stateBean.getTotalConfirmed() + "");
            holder.totalDeath.setText(stateBean.getTotalDeaths() + "");
            holder.totalRecovered.setText(stateBean.getTotalRecovered() + "");
        } else  {holder.totalConfirmed.setText(stateBean.getTotalConfirmed() + "");
            holder.stateName.setText("");
            holder.totalConfirmed.setText(stateBean.getTotalConfirmed() + "");
            holder.totalDeath.setText(stateBean.getTotalDeaths() + "");
            holder.totalRecovered.setText(stateBean.getTotalRecovered() + "");
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        public TextView countryName;
        public TextView totalConfirmed;
        public TextView totalDeath;
        public TextView totalRecovered;
    }

    class ChildHolder {
        public TextView stateName;
        public TextView totalConfirmed;
        public TextView totalDeath;
        public TextView totalRecovered;
    }
}
