package com.example.fishweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishweather.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class CityManageAdapter extends RecyclerView.Adapter<CityManageAdapter.ViewHolder> {

    private List<String> cityList;
    private boolean isModify;
    public CityManageAdapter(List<String> cityList) {
        this.cityList = cityList;
        this.isModify = false;
    }

    public boolean isModify() {
        return isModify;
    }

    public void setModify(boolean modify) {
        isModify = modify;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_manage,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String cityName = cityList.get(position);
        if(isModify){
            holder.action_remove.setVisibility(View.VISIBLE);
            holder.action_move.setVisibility(View.VISIBLE);
        }else{
            holder.action_remove.setVisibility(View.GONE);
            holder.action_move.setVisibility(View.GONE);
        }
        holder.item_city_name.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_city_name;
        ImageView action_remove;
        ImageView action_move;
        public ViewHolder(View itemView) {
            super(itemView);
            item_city_name = (TextView) itemView.findViewById(R.id.item_city_name);
            action_remove = (ImageView) itemView.findViewById(R.id.action_remove);
            action_move = (ImageView) itemView.findViewById(R.id.action_move);
        }
    }
}
