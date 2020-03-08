package com.team2620.coivdashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team2620.coivdashboard.R;
import com.team2620.coivdashboard.bean.GoodsBean;

import java.util.List;

public class GoodListAdapter extends BaseAdapter {

    private List<GoodsBean> goodsBeans;
    private Context context;

    public GoodListAdapter(List<GoodsBean> goodsBeans, Context context) {
        this.goodsBeans = goodsBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return goodsBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_detail_list, null);
        GoodsBean goodsBean = (GoodsBean) getItem(position);
        TextView goodName = view.findViewById(R.id.good_name);
        TextView meanPrice = view.findViewById(R.id.mean_price);
        TextView minPrice = view.findViewById(R.id.min_price);
        TextView maxPrice = view.findViewById(R.id.max_price);
        goodName.setText(goodsBean.getKeyword());
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0");
        meanPrice.setText("$" + df.format(goodsBean.getMean()));
        minPrice.setText("$" + df.format(goodsBean.getMin()));
        maxPrice.setText("$" + df.format(goodsBean.getMax()));
        return view;
    }
}
