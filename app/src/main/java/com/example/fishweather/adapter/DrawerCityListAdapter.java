package com.example.fishweather.adapter;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fishweather.R;
import com.example.fishweather.db.UserCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class DrawerCityListAdapter extends RecyclerView.Adapter<DrawerCityListAdapter.ViewHolder> {

    List<UserCity> userCityList = new ArrayList<>();
    ViewPager weatherInfoView;
    DrawerLayout drawerLayout;
    public DrawerCityListAdapter(ViewPager weatherInfoView, DrawerLayout drawerLayout) {
        super();
        this.weatherInfoView = weatherInfoView;
        this.drawerLayout = drawerLayout;
    }

    public void setUserCityList(List<UserCity> userCityList) {
        if(userCityList != null){
            this.userCityList.clear();
            this.userCityList.addAll(userCityList);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;
        public ViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.item_city_name);
        }

    }

    @Override
    public DrawerCityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_drawer,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = viewHolder.getAdapterPosition();
                if(weatherInfoView.getCurrentItem() != index){
                    weatherInfoView.setCurrentItem(index);
                }
                drawerLayout.closeDrawers();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerCityListAdapter.ViewHolder holder, int position) {

        if(userCityList != null){
            holder.cityName.setText(userCityList.get(position).getCity_name());
        }
    }

    @Override
    public int getItemCount() {
        return userCityList.size();
    }
}
